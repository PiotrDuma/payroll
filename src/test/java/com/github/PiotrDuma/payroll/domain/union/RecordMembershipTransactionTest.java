package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordMembershipTransactionTest {
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());

  @Mock
  private UnionAffiliationRepository repo;


  @Test
  void shouldAddEmployeeToUnionSetAndSave(){
    UnionEntity union = new UnionEntity("name");
    UUID unionId = union.getId();
    RecordMembershipTransaction transaction = new RecordMembershipTransaction(repo, unionId, EMPLOYEE_ID);

    when(this.repo.findById(any())).thenReturn(union);

    assertEquals(0, union.getMembers().size());
    transaction.execute();

    assertEquals(1, union.getMembers().size());
    assertTrue(union.getMembers().contains(EMPLOYEE_ID));
    verify(this.repo, times(1)).save(union);
  }
}