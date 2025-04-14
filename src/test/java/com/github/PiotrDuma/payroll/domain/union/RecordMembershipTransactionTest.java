package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.model.ReceiveEmployee;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordMembershipTransactionTest {
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());

  @Mock
  private ReceiveEmployee employeeRepo;
  @Mock
  private UnionAffiliationRepository repo;

  private RecordMembershipTransaction transaction;
  private UnionEntity union;
  @BeforeEach
  void setUp(){
    this.union = new UnionEntity("name");
    this.transaction = new RecordMembershipTransaction(repo, employeeRepo, union.getId(), EMPLOYEE_ID);
  }

  @Test
  void shouldAddEmployeeToUnionSetAndSave(){
    when(this.repo.findById(any())).thenReturn(Optional.of(union));

    assertEquals(0, union.getMembers().size());
    transaction.execute();

    assertEquals(1, union.getMembers().size());
    assertTrue(union.getMembers().contains(EMPLOYEE_ID));
    verify(this.repo, times(1)).save(union);
  }

  @Test
  void shouldThrowResourceNotFoundWhenUnionIsNotFound(){
    String message = "Union not found";
    UUID uuid = UUID.randomUUID();
    RecordMembershipTransaction transaction = new RecordMembershipTransaction(repo, employeeRepo, uuid, EMPLOYEE_ID);

    when(this.repo.findById(any())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class, () -> this.transaction.execute());

    assertEquals(message, exception.getMessage());
  }

  @Test
  void shouldThrowResourceNotFoundWhenEmployeeIsNotFound(){
    String message = "Employee not found";
    UUID uuid = UUID.randomUUID();
    RecordMembershipTransaction transaction = new RecordMembershipTransaction(repo, employeeRepo, uuid, EMPLOYEE_ID);

    doThrow(new ResourceNotFoundException(message)).when(this.employeeRepo).find(any());

    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class, () -> this.transaction.execute());

    assertEquals(message, exception.getMessage());
  }
}