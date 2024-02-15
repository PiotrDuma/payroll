package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommissionedClassificationEntityTest {
  private static final LocalDate DATE = LocalDate.of(1999, 12,30);
  private static final PaymentPeriod PERIOD =
      new PaymentPeriod(DATE.minusDays(1), DATE.plusDays(1));
  private CommissionedClassification service;

  @BeforeEach
  void setUp(){
    this.service = new AddCommissionedClassification();
  }

  @Test
  void shouldAddSalesReceiptToObject(){
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    Salary salary = new Salary(1000);
    CommissionRate commissionRate = new CommissionRate(15.);
    Amount amount = new Amount(10000);

    CommissionedClassificationEntity classification =
        (CommissionedClassificationEntity)this.service.getClassification(salary, commissionRate);


    classification.addSalesReceipt(employeeId, DATE, amount);
    classification.addSalesReceipt(employeeId, DATE, amount);
    classification.addSalesReceipt(employeeId, DATE, amount);

    assertEquals(3, classification.getSalesReceipts().size());
  }

  @Test
  void shouldCalculateSalaryWithSalesReceipt(){
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    Salary salary = new Salary(1000);
    CommissionRate commissionRate = new CommissionRate(15.);
    Amount amount = new Amount(10000);
    Salary expected = new Salary(2500); //salary + 15% * amount

    PaymentClassification classification = this.service.getClassification(salary, commissionRate);

    if(classification instanceof SalesReceiptProvider){
      ((SalesReceiptProvider) classification).addSalesReceipt(employeeId, DATE, amount);
    }

    Salary result = classification.calculatePay(PERIOD);

    assertTrue(result.equals(expected));
  }

  @Test
  void shouldCalculateSalaryInValidPeriod(){
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    Salary salary = new Salary(1000);
    CommissionRate commissionRate = new CommissionRate(15.);
    Amount amount = new Amount(10000);
    Salary expected = new Salary(2500); //salary + 15% * amount

    PaymentClassification classification = this.service.getClassification(salary, commissionRate);

    if(classification instanceof SalesReceiptProvider){
      ((SalesReceiptProvider) classification).addSalesReceipt(employeeId, DATE, amount);
      ((SalesReceiptProvider) classification).addSalesReceipt(employeeId, DATE.minusDays(4), amount);
      ((SalesReceiptProvider) classification).addSalesReceipt(employeeId, DATE.plusDays(4), amount);
    }

    Salary result = classification.calculatePay(PERIOD);

    assertTrue(result.equals(expected));
  }

  @Test
  void shouldCalculateSalaryWithManySalesReceipt(){
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    Salary salary = new Salary(1000);
    CommissionRate commissionRate = new CommissionRate(15.);
    Amount amount = new Amount(10000);
    Salary expected = new Salary(5500); //salary + 15% * amount * 3

    PaymentClassification classification = this.service.getClassification(salary, commissionRate);

    if(classification instanceof SalesReceiptProvider){
      ((SalesReceiptProvider) classification).addSalesReceipt(employeeId, DATE, amount);
      ((SalesReceiptProvider) classification).addSalesReceipt(employeeId, DATE, amount);
      ((SalesReceiptProvider) classification).addSalesReceipt(employeeId, DATE, amount);
    }

    Salary result = classification.calculatePay(PERIOD);

    assertTrue(result.equals(expected));
  }

  @Test
  void shouldCalculateSalaryWithoutSalesReceipt(){
    Salary salary = new Salary(1000);
    CommissionRate commissionRate = new CommissionRate(15.);

    PaymentClassification classification = this.service.getClassification(salary, commissionRate);

    Salary result = classification.calculatePay(PERIOD);

    assertTrue(result.equals(salary));
  }
}