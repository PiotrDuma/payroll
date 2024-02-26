package com.github.PiotrDuma.payroll.domain.payment.classification.salary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Tag("IntegrationTest")
@ActiveProfiles("test")
class SalariedClassificationIntegrationTest {
  private static final Address ADDRESS = new Address("ADDRESS");
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  private static final Salary SALARY = new Salary(1234);
  @Autowired
  private AddEmployeeTransactionFactory addEmployeeFactory;

  @Autowired
  private ChangeEmployeeService changeEmployeeService;

  @Autowired
  private ReceiveEmployee repo;

  @Test
  void initSalariedEmployeeTransactionShouldSetSalariedClassification(){
    AddEmployeeTransaction transaction = this.addEmployeeFactory.initSalariedEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, SALARY);

    EmployeeId resultId = transaction.execute();

    EmployeeResponse response = this.repo.find(resultId);
    PaymentClassification paymentClassification = response.getPaymentClassification();

    assertTrue(paymentClassification instanceof SalariedClassificationEntity);
    SalariedClassificationEntity entity = (SalariedClassificationEntity) paymentClassification;

    assertEquals(SALARY, entity.getSalary());
  }

  @Test
  void changeSalariedClassificationTransactionShouldSetSalariedClassification(){
    HourlyRate rate = new HourlyRate(12);
    AddEmployeeTransaction transaction = this.addEmployeeFactory.initHourlyEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, rate);

    EmployeeId resultId = transaction.execute();

    EmployeeResponse response = this.repo.find(resultId);
    PaymentClassification paymentClassification = response.getPaymentClassification();
    assertFalse(paymentClassification instanceof SalariedClassificationEntity);

    this.changeEmployeeService.changeSalariedClassificationTransaction(resultId, SALARY);

    EmployeeResponse response2 = this.repo.find(resultId);
    PaymentClassification paymentClassification2 = response2.getPaymentClassification();
    assertTrue(paymentClassification2 instanceof SalariedClassificationEntity);

    SalariedClassificationEntity entity = (SalariedClassificationEntity) paymentClassification2;

    assertEquals(SALARY, entity.getSalary());
  }

  @Test
  void changeSalariedClassificationTransactionShouldThrowWhenEmployeeIdIsInvalid(){
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());

    assertThrows(ResourceNotFoundException.class,
        () -> this.changeEmployeeService.changeSalariedClassificationTransaction(employeeId, SALARY));
  }
}