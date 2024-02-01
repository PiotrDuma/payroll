package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.salary.api.SalariedClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddSalariedEmployeeTransactionTest {
  private static final Address ADDRESS = new Address("address");
  private static final EmployeeName NAME = new EmployeeName("EMPLOYEE NAME");
  private static final Salary SALARY = new Salary(1000);
  @Mock
  private PaymentScheduleFactory scheduleFactory;
  @Mock
  private PaymentMethodFactory methodFactory;
  @Mock
  private SalariedClassification classification;

  private EmployeeRepository repo;

  private AddSalariedEmployeeTransaction transaction;

  @BeforeEach
  void setUp(){
    this.repo = new MockEmployeeRepository();
    this.transaction = new AddSalariedEmployeeTransaction(repo, classification, scheduleFactory,
        methodFactory, ADDRESS, NAME, SALARY);
  }

  @Test
  void transactionShouldAddObjectToRepository(){
    EmployeeId expectedId = transaction.execute();

    assertEquals(1, repo.findAll().size());
    assertTrue(this.repo.findAll().stream().findFirst().isPresent());
    Employee employee = this.repo.findAll().stream().findFirst().get();

    assertEquals(ADDRESS, employee.getAddress());
    assertEquals(NAME, employee.getName());
    assertEquals(expectedId, employee.getId());
  }

  @Test
  void transactionShouldCallSalariedClassificationService(){
    transaction.execute();

    assertEquals(1, repo.findAll().size());
    verify(this.classification, times(1)).getClassification(SALARY);
  }

  @Test
  void transactionShouldCallMonthlyMethodInScheduleFactory(){
    transaction.execute();

    assertEquals(1, repo.findAll().size());
    verify(this.scheduleFactory, times(1)).getMonthlySchedule();
  }

  @Test
  void transactionShouldCallHoldMethodInPaymentMethodFactory(){
    transaction.execute();

    assertEquals(1, repo.findAll().size());
    verify(this.methodFactory, times(1)).getHoldPaymentMethod();
  }
}