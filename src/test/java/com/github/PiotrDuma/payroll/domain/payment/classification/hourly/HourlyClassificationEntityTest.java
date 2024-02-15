package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HourlyClassificationEntityTest {
  private static final HourlyRate HOURLY_RATE = new HourlyRate(5.5);
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());
  private static final LocalDate DATE = LocalDate.of(1999, 12,30);

  private HourlyClassificationEntity entity;

  @BeforeEach
  void setUp(){
    this.entity = new HourlyClassificationEntity(HOURLY_RATE);
  }

  @Test
  void shouldAddTimeCard(){
    Hours hours = new Hours(5.5);

    assertEquals(0, this.entity.getTimeCards().size());
    this.entity.addOrUpdateTimeCard(EMPLOYEE_ID, DATE, hours);
    assertEquals(1, this.entity.getTimeCards().size());
    assertTrue(this.entity.getTimeCards().stream().findAny().isPresent());

    TimeCard result = this.entity.getTimeCards().stream().findAny().get();
    assertEquals(DATE, result.getDate());
    assertEquals(hours, result.getHours());
  }

  @Test
  void shouldUpdateHoursIfTimeCardIsPresent(){
    Hours hours = new Hours(5.5);
    Hours newHours = new Hours(7d);

    assertEquals(0, this.entity.getTimeCards().size());
    this.entity.addOrUpdateTimeCard(EMPLOYEE_ID, DATE, hours);
    assertEquals(1, this.entity.getTimeCards().size());
    this.entity.addOrUpdateTimeCard(EMPLOYEE_ID, DATE, newHours);
    assertEquals(1, this.entity.getTimeCards().size());
    assertTrue(this.entity.getTimeCards().stream().findAny().isPresent());

    TimeCard result = this.entity.getTimeCards().stream().findAny().get();
    assertEquals(DATE, result.getDate());
    assertEquals(newHours, result.getHours());
  }

  @Test
  void shouldCountPartTimeSalary(){
    Salary expected = new Salary(30.25); // 5.5 * 5.5

    PaymentPeriod paymentPeriod = new PaymentPeriod(DATE.minusDays(1), DATE.plusDays(1));
    Hours hours = new Hours(5.5);
    this.entity.addOrUpdateTimeCard(EMPLOYEE_ID, DATE, hours);

    Salary result = this.entity.calculatePay(paymentPeriod);

    assertTrue(result.equals(expected));
  }

  @Test
  void shouldNotCountTimeCardsBeforePeymentPeriod(){
    Salary expected = new Salary(30.25); // 5.5 * 5.5

    PaymentPeriod paymentPeriod = new PaymentPeriod(DATE.minusDays(1), DATE.plusDays(1));
    Hours hours = new Hours(5.5);
    this.entity.addOrUpdateTimeCard(EMPLOYEE_ID, DATE, hours);
    this.entity.addOrUpdateTimeCard(EMPLOYEE_ID, DATE.minusDays(3), hours);

    Salary result = this.entity.calculatePay(paymentPeriod);

    assertTrue(result.equals(expected));
  }

  @Test
  void shouldNotCountTimeCardsAfterPeymentPeriod(){
    Salary expected = new Salary(30.25); // 5.5 * 5.5

    PaymentPeriod paymentPeriod = new PaymentPeriod(DATE.minusDays(1), DATE.plusDays(1));
    Hours hours = new Hours(5.5);
    this.entity.addOrUpdateTimeCard(EMPLOYEE_ID, DATE, hours);
    this.entity.addOrUpdateTimeCard(EMPLOYEE_ID, DATE.plusDays(3), hours);

    Salary result = this.entity.calculatePay(paymentPeriod);

    assertTrue(result.equals(expected));
  }

  @Test
  void shouldCountExtraOvertime(){ //every hour more than 8 is paid extra 1.5
    Salary expected = new Salary(68.750); // 8 * 5.5 + 3 * 5.5 * 1.5

    PaymentPeriod paymentPeriod = new PaymentPeriod(DATE.minusDays(1), DATE.plusDays(1));
    Hours hours = new Hours(11d);
    this.entity.addOrUpdateTimeCard(EMPLOYEE_ID, DATE, hours);

    Salary result = this.entity.calculatePay(paymentPeriod);

    assertTrue(result.equals(expected));
  }
}