package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import org.junit.jupiter.api.Test;

class EmployeeTest {

  @Test
  void getSchedule() {
    String expectedMessage = "Missing employee's schedule method";
    Employee employee = new Employee(new EmployeeName("NAME"), new Address("ADDRESS"));

    Exception exception = assertThrows(RuntimeException.class, employee::getSchedule);
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void getPaymentClassification() {
    String expectedMessage = "Missing employee's payment classification method";
    Employee employee = new Employee(new EmployeeName("NAME"), new Address("ADDRESS"));

    Exception exception = assertThrows(RuntimeException.class, employee::getPaymentClassification);
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void getPaymentMethodShouldThrowWhenFieldIsNull() {
    String expectedMessage = "Missing employee's payment method";
    Employee employee = new Employee(new EmployeeName("NAME"), new Address("ADDRESS"));

    Exception exception = assertThrows(RuntimeException.class, employee::getPaymentMethod);
    assertEquals(expectedMessage, exception.getMessage());
  }
}