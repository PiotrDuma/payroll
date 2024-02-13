package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReceiveEmployeeServiceTest {
  @Mock
  private EmployeeRepository repo;

  private ReceiveEmployee service;

  @BeforeEach
  void setUp(){
    this.service = new ReceiveEmployeeService(repo);
  }

  @Test
  void findShouldThrowResourceNotFoundExceptionWhenEmployeeNotFound(){
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    String expectedMessage = "Employee with id: " + employeeId + " not found";

    when(this.repo.findById(any())).thenReturn(Optional.empty());

    Exception result = assertThrows(ResourceNotFoundException.class, () -> this.service.find(employeeId));

    assertEquals(expectedMessage, result.getMessage());
  }

  @Test
  void findShouldReturnEmployeeResponseObject(){
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    Employee employee = mock(Employee.class);

    when(this.repo.findById(any())).thenReturn(Optional.of(employee));

    EmployeeResponse result = this.service.find(employeeId);

    assertNotNull(result);
  }

  @Test
  void findAllShouldReturnListOfEmployeeResponseObjects(){
    Employee employee1 = mock(Employee.class);
    Employee employee2 = mock(Employee.class);
    List<Employee> repoResponse = List.of(employee1, employee2);

    when(this.repo.findAll()).thenReturn(repoResponse);

    List<EmployeeResponse> result = this.service.findAll();

    assertNotNull(result);
    assertEquals(2, result.size());
  }
}