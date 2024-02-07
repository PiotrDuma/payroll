package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UnionEntityTest {

  @Test
  void shouldRemoveEmployeeIdFromCollection(){
    UnionEntity union = new UnionEntity("NAME");
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    union.addMember(employeeId);

    assertEquals(1, union.getMembers().size());

    union.removeMembership(employeeId);
    assertEquals(0, union.getMembers().size());
  }
}