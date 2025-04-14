package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
  @Mock
  private EmployeeRepository repo;

  private AddCommissionedEmployeeTransaction transaction;

  @BeforeEach
  void setUp(){
    this.transaction = new AddCommissionedEmployeeTransaction(repo, classification, scheduleFactory,
        methodFactory, ADDRESS, NAME, SALARY, COMMISSION_RATE);
  }

  @Test
  void transactionShouldAddObjectToRepository(){
    ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);

    EmployeeId expectedId = transaction.execute();

    verify(this.repo, times(1)).save(captor.capture());
    Employee employee = captor.getValue();

    assertEquals(ADDRESS, employee.getAddress());
    assertEquals(NAME, employee.getName());
    assertEquals(expectedId, employee.getId());
  }

  @Test
  void transactionShouldCallHourlyClassificationService(){
    transaction.execute();

    verify(this.classification, times(1)).getClassification(SALARY, COMMISSION_RATE);
  }

  @Test
  void transactionShouldCallMonthlyMethodInScheduleFactory(){
    transaction.execute();

    verify(this.scheduleFactory, times(1)).getBiweeklySchedule();
  }

  @Test
  void transactionShouldCallHoldMethodInPaymentMethodFactory(){
    transaction.execute();

    verify(this.methodFactory, times(1)).getHoldPaymentMethod();
  }
}