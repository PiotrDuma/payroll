package com.github.PiotrDuma.payroll.domain.payment.classification.salary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.salary.api.SalariedClassification;
import com.github.PiotrDuma.payroll.domain.payment.schedule.PaymentPeriod;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalariedClassificationTest {
  private static final LocalDate DATE = LocalDate.of(1999, 12,30);
  private SalariedClassification salariedClassification;

  @BeforeEach
  void setUp(){
    this.salariedClassification = new AddSalariedClassification();
  }

  @Test
  void shouldCalculateSalaryOfCreatedObject(){
    Salary salary = new Salary(new BigDecimal(1000));
    PaymentPeriod period = new PaymentPeriod(DATE, DATE.plusDays(30));
    PaymentClassification classification = this.salariedClassification.getClassification(salary);

    Salary result = classification.calculatePay(period);

    assertEquals(salary, result);
  }

  @Test
  void shouldThrowGetClassificationWhenSalaryIsNull() throws Exception{
    String message = "Classification must contain salary object";
    Salary salary = null;

    Exception exception = assertThrows(IllegalArgumentException.class,
        () -> this.salariedClassification.getClassification(salary));
    assertEquals(message, exception.getMessage());
  }
}