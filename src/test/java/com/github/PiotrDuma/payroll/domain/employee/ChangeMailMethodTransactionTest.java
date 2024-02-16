package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChangeMailMethodTransactionTest {
  private static final Address ADDRESS = new Address("ADDRESS");
  @Mock
  private EmployeeRepository repo;
  @Mock
  private PaymentMethodFactory methodFactory;
  private Employee employee;
  private ChangeMailMethodTransaction transaction;

  @BeforeEach
  void setUp(){
    this.employee = new Employee(new EmployeeName("NAME"), ADDRESS);
    this.transaction = new ChangeMailMethodTransaction(repo, methodFactory, employee.getId(), ADDRESS);
  }

  @Test
  void shouldSetSalariedClassificationAndMonthlyScheduleThenSave(){
    PaymentMethod newMethod = mock(PaymentMethod.class);

    when(this.repo.findById(any())).thenReturn(Optional.of(employee));
    when(this.repo.save(any())).thenReturn(this.employee);
    when(this.methodFactory.getMailPaymentMethod(ADDRESS)).thenReturn(newMethod);

    Employee result = (Employee) this.transaction.execute();

    assertEquals(newMethod, result.getPaymentMethod());
    verify(this.repo, times(1)).save(this.employee);
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenEmployeeNotFound(){
    String message = "Employee with id: " + this.employee.getId() + " not found";
    when(this.repo.findById(any())).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> this.transaction.execute());
    assertEquals(message, exception.getMessage());
  }
}