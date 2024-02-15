package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import java.time.LocalDate;
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

  @Test
  void shouldAddMembersCharge(){
    Amount amount = new Amount(123);
    LocalDate date = LocalDate.of(2000, 1, 2);

    UnionEntity union = new UnionEntity("NAME");

    assertEquals(0, union.getCharges().size());
    UnionCharge unionCharge = union.addMembersCharge(amount, date);
    assertEquals(1, union.getCharges().size());

    assertEquals(amount, unionCharge.getAmount());
    assertEquals(date, unionCharge.getDate());
  }
}