package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChangeCommissionedClassificationTransactionTest {
  private static final Salary SALARY = new Salary(1234);
  private static final CommissionRate COMMISSION_RATE = new CommissionRate(12.5);
  @Mock
  private EmployeeRepository repo;
  @Mock
  private CommissionedClassification classification;
  @Mock
  private PaymentScheduleFactory scheduleFactory;
  private Employee employee;
  private ChangeCommissionedClassificationTransaction transaction;

  @BeforeEach
  void setUp(){
    this.employee = new Employee(new EmployeeName("NAME"), new Address("ADDRESS"));
    this.transaction = new ChangeCommissionedClassificationTransaction(repo, classification,
        scheduleFactory, employee.getId(), SALARY, COMMISSION_RATE);
  }

  @Test
  void shouldSetSalariedClassificationAndMonthlyScheduleThenSave(){
    PaymentClassification newClassification = mock(PaymentClassification.class);
    PaymentSchedule newSchedule = mock(PaymentSchedule.class);

    when(this.repo.findById(any())).thenReturn(Optional.of(employee));
    when(this.scheduleFactory.getBiweeklySchedule()).thenReturn(newSchedule);
    when(this.classification.getClassification(SALARY, COMMISSION_RATE)).thenReturn(newClassification);
    when(this.repo.save(any())).thenReturn(this.employee);

    Employee result = (Employee) this.transaction.execute();

    assertEquals(newClassification, result.getPaymentClassification());
    assertEquals(newSchedule, result.getSchedule());
    verify(this.repo, times(1)).save(this.employee);
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenEmployeeNotFound(){
    String message = "Employee with id: " + this.employee.getId() + " not found";
    when(this.repo.findById(any())).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> this.transaction.execute());
    assertEquals(message, exception.getMessage());
  }
}