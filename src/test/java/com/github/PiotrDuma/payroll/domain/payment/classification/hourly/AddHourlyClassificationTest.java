package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentPeriod;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddHourlyClassificationTest {
  private static final HourlyRate HOURLY_RATE = new HourlyRate(5.5);
  private static final LocalDate DATE = LocalDate.of(1999, 12,30);
  private HourlyClassification service;

  @BeforeEach
  void setUp(){
    this.service = new AddHourlyClassification();
  }

  @Test
  void shouldAddTimeCardsToObject(){
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    Salary expected = new Salary(44); // 8 * 5.5
    Hours hours = new Hours(8d);
    PaymentPeriod paymentPeriod = new PaymentPeriod(DATE.minusDays(1), DATE.plusDays(1));
    PaymentClassification classification = this.service.getClassification(HOURLY_RATE);

    if(classification instanceof TimeCardProvider){
      ((TimeCardProvider) classification).addOrUpdateTimeCard(employeeId, DATE, hours);
    }

    Salary result = classification.calculatePay(paymentPeriod);

    assertTrue(result.equals(expected));
  }
}