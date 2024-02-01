package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddCommissionedEmployeeTransactionTest {
  private static final Address ADDRESS = new Address("address");
  private static final EmployeeName NAME = new EmployeeName("EMPLOYEE NAME");
  private static final Salary SALARY = new Salary(1000);
  private static final CommissionRate COMMISSION_RATE = new CommissionRate(15.);

  @Mock
  private PaymentScheduleFactory scheduleFactory;
  @Mock
  private PaymentMethodFactory methodFactory;
  @Mock
  private CommissionedClassification classification;

  private EmployeeRepository repo;

  private AddCommissionedEmployeeTransaction transaction;

  @BeforeEach
  void setUp(){
    this.repo = new MockEmployeeRepository();
    this.transaction = new AddCommissionedEmployeeTransaction(repo, classification, scheduleFactory,
        methodFactory, ADDRESS, NAME, SALARY, COMMISSION_RATE);
  }

  @Test
  void transactionShouldAddObjectToRepository(){
    transaction.execute();

    assertEquals(1, repo.findAll().size());
    assertTrue(this.repo.findAll().stream().findFirst().isPresent());
    Employee employee = this.repo.findAll().stream().findFirst().get();

    assertEquals(ADDRESS, employee.getAddress());
    assertEquals(NAME, employee.getName());
  }

  @Test
  void transactionShouldCallHourlyClassificationService(){
    transaction.execute();

    assertEquals(1, repo.findAll().size());
    verify(this.classification, times(1)).getClassification(SALARY, COMMISSION_RATE);
  }

  @Test
  void transactionShouldCallMonthlyMethodInScheduleFactory(){
    transaction.execute();

    assertEquals(1, repo.findAll().size());
    verify(this.scheduleFactory, times(1)).getBiweeklySchedule();
  }

  @Test
  void transactionShouldCallHoldMethodInPaymentMethodFactory(){
    transaction.execute();

    assertEquals(1, repo.findAll().size());
    verify(this.methodFactory, times(1)).getHoldPaymentMethod();
  }
}