package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@Tag("IntegrationTest")
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.ANY)
class HourlyClassificationIntegrationTest {
  private static final Address ADDRESS = new Address("ADDRESS");
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  private static final HourlyRate HOURLY_RATE = new HourlyRate(12);
  @Autowired
  private AddEmployeeTransactionFactory addEmployeeFactory;

  @Autowired
  private ChangeEmployeeService changeEmployeeService;

  @Autowired
  private ReceiveEmployee repo;

  @Test
  void addEmployeeFactoryShouldSetHourlyClassification(){
    AddEmployeeTransaction transaction = this.addEmployeeFactory.initHourlyEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, HOURLY_RATE);

    EmployeeId resultId = transaction.execute();

    assertEquals(1, this.repo.findAll().size());

    EmployeeResponse response = this.repo.find(resultId);
    PaymentClassification paymentClassification = response.getPaymentClassification();

    assertTrue(paymentClassification instanceof HourlyClassificationEntity);
    HourlyClassificationEntity entity = (HourlyClassificationEntity) paymentClassification;

    assertEquals(HOURLY_RATE, entity.getHourlyRate());
  }

  @Test
  void changeEmployeeServiceShouldSetHourlyClassification(){
    Salary salary = new Salary(1234);
    AddEmployeeTransaction transaction = this.addEmployeeFactory.initSalariedEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, salary);

    EmployeeId resultId = transaction.execute();

    assertEquals(1, this.repo.findAll().size());

    EmployeeResponse response = this.repo.find(resultId);
    PaymentClassification paymentClassification = response.getPaymentClassification();
    assertFalse(paymentClassification instanceof HourlyClassificationEntity);

    this.changeEmployeeService.changeHourlyClassificationTransaction(resultId, HOURLY_RATE);

    EmployeeResponse response2 = this.repo.find(resultId);
    PaymentClassification paymentClassification2 = response2.getPaymentClassification();
    assertTrue(paymentClassification2 instanceof HourlyClassificationEntity);

    HourlyClassificationEntity entity = (HourlyClassificationEntity) paymentClassification2;

    assertEquals(HOURLY_RATE, entity.getHourlyRate());
  }
}