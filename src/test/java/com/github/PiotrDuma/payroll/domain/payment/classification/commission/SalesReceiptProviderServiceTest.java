package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SalesReceiptProviderServiceTest {
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());
  private static final LocalDate DATE = LocalDate.of(2000, 1, 1);
  private static final Amount AMOUNT = new Amount(1000);

  @Mock
  private ReceiveEmployee employeeRepo;
  private SalesReceiptProvider service;
  private EmployeeResponse employeeResponse;

  @BeforeEach
  void setUp(){
    this.service = new SalesReceiptProviderService(employeeRepo);
    this.employeeResponse = mock(EmployeeResponse.class);
    when(this.employeeRepo.find(any())).thenReturn(employeeResponse);
  }

  @Test
  void addSalesReceiptShouldThrowInvalidArgumentExceptionWhenClassificationCastingFails(){
    String message = "Employee " + EMPLOYEE_ID + " cannot record sales receipts";
    PaymentClassification failureClassification = new MockPaymentClassification();
    when(employeeResponse.getPaymentClassification()).thenReturn(failureClassification);

    Exception exception = assertThrows(InvalidArgumentException.class,
        () -> this.service.addSalesReceipt(EMPLOYEE_ID, DATE, AMOUNT));
    assertEquals(message, exception.getMessage());
  }

  @Test
  void addSalesReceiptShouldAddObjectToClassificationEntityCollection(){
    CommissionedClassificationEntity classification =
        new CommissionedClassificationEntity(new Salary(1234), new CommissionRate(12.));
    when(employeeResponse.getPaymentClassification()).thenReturn(classification);

    this.service.addSalesReceipt(EMPLOYEE_ID, DATE, AMOUNT);

    assertEquals(1, classification.getSalesReceipts().size());
    SalesReceipt timecard = classification.getSalesReceipts().iterator().next();

    assertEquals(DATE, timecard.getDate());
    assertEquals(AMOUNT, timecard.getAmount());
    assertEquals(EMPLOYEE_ID, timecard.getEmployeeId());
  }

  @Test
  void addSalesReceiptShouldAddObjectsToClassificationEntityCollectionWithTheSameDate(){
    CommissionedClassificationEntity classification =
        new CommissionedClassificationEntity(new Salary(1234), new CommissionRate(12.));
    when(employeeResponse.getPaymentClassification()).thenReturn(classification);

    this.service.addSalesReceipt(EMPLOYEE_ID, DATE, AMOUNT);
    this.service.addSalesReceipt(EMPLOYEE_ID, DATE, AMOUNT);

    assertEquals(2, classification.getSalesReceipts().size());
  }

  private class MockPaymentClassification implements PaymentClassification{
    @Override
    public Salary calculatePay(PaymentPeriod paymentPeriod) {
      return null;
    }
  }
}