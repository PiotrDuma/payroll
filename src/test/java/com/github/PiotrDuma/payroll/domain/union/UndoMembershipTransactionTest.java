package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UndoMembershipTransactionTest {
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());

  @Mock
  private UnionAffiliationRepository repo;

  @Spy
  private Set<EmployeeId> mockSet;

  private UndoMembershipTransaction transaction;
  private UnionEntity union;

  @BeforeEach
  void setUp(){
    this.union = new UnionEntity("NAME");
    this.transaction = new UndoMembershipTransaction(repo, union.getId(), EMPLOYEE_ID);
    this.mockSet = new HashSet<>();
  }

  @Test
  void shouldRemoveEmployeeIdFromUnion(){
    when(this.repo.findById(any())).thenReturn(Optional.of(union));


    assertEquals(0, union.getMembers().size());
    union.addMember(EMPLOYEE_ID);
    assertEquals(1, union.getMembers().size());

    this.transaction.execute();
    assertEquals(0, union.getMembers().size());
    verify(this.repo, times(1)).save(union);
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenUnionIsNotFound(){
    String message = "Union not found";
    when(this.repo.findById(any())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class, () -> this.transaction.execute());
    assertEquals(message, exception.getMessage());
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenEmployeeIsNotAMember(){
    String message = "Employee "+ EMPLOYEE_ID + " is not affiliated with this union";
    when(this.repo.findById(any())).thenReturn(Optional.of(union));

    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class, () -> this.transaction.execute());
    assertEquals(message, exception.getMessage());
  }
}