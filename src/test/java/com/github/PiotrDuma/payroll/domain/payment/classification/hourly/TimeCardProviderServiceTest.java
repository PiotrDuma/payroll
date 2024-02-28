package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeCardProviderServiceTest {
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());
  private static final LocalDate DATE = LocalDate.of(2000, 1, 1);
  private static final Hours HOURS = new Hours(10.);
  @Mock
  private ReceiveEmployee employeeRepo;

  private TimeCardProvider service;
  private EmployeeResponse employeeResponse;

  @BeforeEach
  void setUp(){
    this.service = new TimeCardProviderService(employeeRepo);
    this.employeeResponse = mock(EmployeeResponse.class);
    when(this.employeeRepo.find(any())).thenReturn(employeeResponse);
  }

  @Test
  void addTimeCardShouldThrowInvalidArgumentExceptionWhenHourlyClassificationFails(){
    String message = "Employee " + EMPLOYEE_ID + " cannot record time cards";
    PaymentClassification failureClassification = new MockPaymentClassification();
    when(employeeResponse.getPaymentClassification()).thenReturn(failureClassification);

    Exception exception = assertThrows(InvalidArgumentException.class,
        () -> this.service.addOrUpdateTimeCard(EMPLOYEE_ID, DATE, HOURS));
    assertEquals(message, exception.getMessage());
  }

  @Test
  void addTimeCardShouldAddNewTimeCard(){
    HourlyClassificationEntity classification = new HourlyClassificationEntity(new HourlyRate(12));
    when(employeeResponse.getPaymentClassification()).thenReturn(classification);

    this.service.addOrUpdateTimeCard(EMPLOYEE_ID, DATE, HOURS);

    assertEquals(1, classification.getTimeCards().size());
    TimeCard timecard = classification.getTimeCards().iterator().next();

    assertEquals(DATE, timecard.getDate());
    assertEquals(HOURS, timecard.getHours());
    assertEquals(EMPLOYEE_ID, timecard.getEmployeeId());
  }

  private class MockPaymentClassification implements PaymentClassification{
    @Override
    public Salary calculatePay(PaymentPeriod paymentPeriod) {
      return null;
    }
  }
}