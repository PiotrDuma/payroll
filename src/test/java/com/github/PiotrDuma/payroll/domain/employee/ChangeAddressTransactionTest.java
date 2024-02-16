package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChangeAddressTransactionTest {
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("EMP_NAME");
  private static final Address EMPLOYEE_ADDRESS = new Address("ADDRESS");
  private static final Address NEW_ADDRESS = new Address("new address");
  @Mock
  private EmployeeRepository repo;
  private Employee employee;
  private ChangeAddressTransaction transaction;

  @BeforeEach
  void setUp(){
    this.employee = new Employee(EMPLOYEE_NAME, EMPLOYEE_ADDRESS);
    this.transaction = new ChangeAddressTransaction(repo, employee.getId(), NEW_ADDRESS);
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenEmployeeNotFound(){
    String message = "Employee with id: " + this.employee.getId() + " not found";
    when(this.repo.findById(any())).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> this.transaction.execute());
    assertEquals(message, exception.getMessage());
  }

  @Test
  void shouldChangeEmployeeAddressAndSaveToRepository(){
    when(this.repo.findById(any())).thenReturn(Optional.of(this.employee));
    when(this.repo.save(any())).thenReturn(employee);

    Employee result = (Employee) this.transaction.execute();

    assertEquals(NEW_ADDRESS, result.getAddress());
    verify(this.repo, times(1)).save(employee);
  }
}