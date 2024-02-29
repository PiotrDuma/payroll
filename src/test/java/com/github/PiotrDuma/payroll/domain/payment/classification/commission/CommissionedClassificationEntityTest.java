package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommissionedClassificationEntityTest {
  private static final LocalDate DATE = LocalDate.of(1999, 12,30);
  private static final PaymentPeriod PERIOD =
      new PaymentPeriod(DATE.minusDays(1), DATE.plusDays(1));
  private static final Salary SALARY = new Salary(1000);
  private static final CommissionRate COMMISSION_RATE = new CommissionRate(15.);
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());
  private CommissionedClassificationEntity classification;

  @BeforeEach
  void setUp(){
    this.classification = new CommissionedClassificationEntity(SALARY, COMMISSION_RATE);
  }

  @Test
  void shouldAddSalesReceiptToObject(){
    Amount amount = new Amount(10000);

    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE, amount);
    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE, amount);
    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE, amount);

    assertEquals(3, classification.getSalesReceipts().size());
  }

  @Test
  void shouldCalculateSalaryWithSalesReceipt(){
    Amount amount = new Amount(10000);
    Salary expected = new Salary(2500); //salary + 15% * amount

    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE, amount);

    Salary result = classification.calculatePay(PERIOD);

    assertEquals(expected, result);
  }

  @Test
  void shouldCalculateSalaryInValidPeriod(){
    Amount amount = new Amount(10000);
    Salary expected = new Salary(2500); //salary + 15% * amount

    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE, amount);
    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE.minusDays(2), amount);
    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE.plusDays(2), amount);

    Salary result = classification.calculatePay(PERIOD);

    assertEquals(expected, result);
  }

  @Test
  void shouldCalculateSalaryWithManySalesReceipt(){
    Amount amount = new Amount(10000);
    Salary expected = new Salary(5500); //salary + 15% * amount * 3

    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE.minusDays(1), amount);
    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE, amount);
    this.classification.addSalesReceipt(EMPLOYEE_ID, DATE.plusDays(1), amount);

    Salary result = classification.calculatePay(PERIOD);

    assertEquals(expected, result);
  }

  @Test
  void shouldCalculateSalaryWithoutSalesReceipt(){
    Salary result = this.classification.calculatePay(PERIOD);

    assertEquals(SALARY, result);
  }
}