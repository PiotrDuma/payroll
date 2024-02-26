package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
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
class ChangeEmployeeIntegrationTest {
  private static final Address ADDRESS = new Address("ADDRESS");
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  @Autowired
  private AddEmployeeTransactionFactory addEmployeeFactory;

  @Autowired
  private ChangeEmployeeService changeEmployeeService;

  @Autowired
  private ReceiveEmployee repo;

  @Test
  void changeNameTransactionShouldSetName(){
    EmployeeName name = new EmployeeName("new name");
    EmployeeId employeeId = initEmployee();

    changeEmployeeService.changeNameTransaction(employeeId, name);

    EmployeeResponse employee = repo.find(employeeId);
    assertEquals(name, employee.getName());
  }

  @Test
  void changeNameTransactionShouldThrowWhenEmployeeIdIsInvalid(){
    EmployeeName name = new EmployeeName("new name");
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());

    assertThrows(ResourceNotFoundException.class,
        () -> changeEmployeeService.changeNameTransaction(employeeId, name));
  }

  @Test
  void changeAddressTransactionShouldSetAddress(){
    Address address = new Address("new address");
    EmployeeId employeeId = initEmployee();

    changeEmployeeService.changeAddressTransaction(employeeId, address);

    EmployeeResponse employee = repo.find(employeeId);
    assertEquals(address, employee.getAddress());
  }

  @Test
  void changeAddressTransactionShouldThrowWhenEmployeeIdIsInvalid(){
    Address address = new Address("new address");
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());

    assertThrows(ResourceNotFoundException.class,
        () -> changeEmployeeService.changeAddressTransaction(employeeId, address));
  }

  private EmployeeId initEmployee(){
    AddEmployeeTransaction transaction = addEmployeeFactory.initSalariedEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, new Salary(123));
    return transaction.execute();
  }
}