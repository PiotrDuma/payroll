package com.github.PiotrDuma.payroll.domain.employee.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.model.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
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
class AddEmployeeTransactionIntegrationTest {
  private static final Address ADDRESS = new Address("ADDRESS");
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  @Autowired
  private AddEmployeeTransactionFactory factory;

  @Autowired
  private ReceiveEmployee repo;

  @Test
  void factoryShouldExecuteTransactionToAddHourlyEmployee(){
    HourlyRate hourlyRate = new HourlyRate(12);
    AddEmployeeTransaction transaction = this.factory.initHourlyEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, hourlyRate);

    EmployeeId resultId = transaction.execute();

    assertEquals(1, this.repo.findAll().size());

    EmployeeResponse response = this.repo.find(resultId);
    assertEquals(ADDRESS, response.getAddress());
    assertEquals(EMPLOYEE_NAME, response.getName());
  }

  @Test
  void factoryShouldExecuteTransactionToAddSalariedEmployee(){
    Salary salary = new Salary(1234);
    AddEmployeeTransaction transaction = this.factory.initSalariedEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, salary);

    EmployeeId resultId = transaction.execute();

    assertEquals(1, this.repo.findAll().size());

    EmployeeResponse response = this.repo.find(resultId);
    assertEquals(ADDRESS, response.getAddress());
    assertEquals(EMPLOYEE_NAME, response.getName());
  }

  @Test
  void factoryShouldExecuteTransactionToAddCommissionedEmployee(){
    Salary salary = new Salary(1234);
    CommissionRate rate = new CommissionRate(12);
    AddEmployeeTransaction transaction = this.factory.initCommissionedEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, salary, rate);

    EmployeeId resultId = transaction.execute();

    assertEquals(1, this.repo.findAll().size());

    EmployeeResponse response = this.repo.find(resultId);
    assertEquals(ADDRESS, response.getAddress());
    assertEquals(EMPLOYEE_NAME, response.getName());
  }
}