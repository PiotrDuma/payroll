package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Tag("IntegrationTest")
@ActiveProfiles("test")
public class UnionPersistenceIntegrationTest {

  @Autowired
  private UnionAffiliationRepository repo;

  @Test
  void shouldAddNewUnionToDatabase() {
    UnionEntity expected = new UnionEntity("NAME");

    UnionEntity result = this.repo.save(expected);

    assertEquals(1, repo.findAll().size());
    assertTrue(repo.findById(result.getId()).isPresent());
    assertTrue(result.equals(expected));
  }

  @Test
  void shouldAddNewMemberToUnion() {
    UnionEntity expected = new UnionEntity("NAME");
    EmployeeId member = new EmployeeId(UUID.randomUUID());
    expected.addMember(member);

    UnionEntity result = this.repo.save(expected);

    assertTrue(repo.findById(result.getId()).isPresent());
    UnionEntity union = repo.findById(result.getId()).get();
    assertEquals(1, union.getMembers().size());
    assertTrue(union.getMembers().contains(member));
  }

  @Test
  void shouldRemoveMemberFromUnion(){
    UnionEntity union = new UnionEntity("NAME");
    EmployeeId member = new EmployeeId(UUID.randomUUID());
    union.addMember(member);

    UnionEntity savedUnion = this.repo.save(union);

    assertTrue(repo.findById(savedUnion.getId()).isPresent());
    UnionEntity unionWithMember = repo.findById(union.getId()).get();
    assertEquals(1, unionWithMember.getMembers().size());
    assertTrue(unionWithMember.getMembers().contains(member));

    unionWithMember.removeMembership(member);
    UnionEntity result2 = this.repo.save(unionWithMember);

    assertTrue(repo.findById(union.getId()).isPresent());
    UnionEntity unionWithoutMember = repo.findById(union.getId()).get();
    assertEquals(0, unionWithoutMember.getMembers().size());
  }

  @Test
  void shouldThrowResourceNotFoundExceptionWhenEmployeeIsNotUnionMember(){
    UnionEntity union = new UnionEntity("NAME");
    EmployeeId member = new EmployeeId(UUID.randomUUID());

    UnionEntity savedUnion = this.repo.save(union);
    String message = "Employee "+ member.toString() + " is not a member of union("
        + savedUnion.getId().toString() + ")";

    Exception exception = assertThrows(InvalidArgumentException.class,
        () -> savedUnion.removeMembership(member));

    assertEquals(message, exception.getMessage());
  }

  @Test
  void shouldAddNewChargeToUnionEntity() {
    Amount amount = new Amount(123);
    LocalDate date = LocalDate.of(2000, 1, 1);
    UnionEntity union = new UnionEntity("NAME");

    UnionCharge savedCharge = union.addMembersCharge(amount, date);
    UnionEntity savedUnion = this.repo.save(union);

    assertTrue(repo.findById(savedUnion.getId()).isPresent());

    UnionEntity unionEntity = repo.findById(savedUnion.getId()).get();
    assertEquals(savedUnion, unionEntity);
    assertTrue(unionEntity.getCharges().contains(savedCharge));

    assertTrue(union.getCharges().stream().findFirst().isPresent());
    UnionCharge unionCharge = union.getCharges().stream().findFirst().get();

    assertEquals(amount, unionCharge.getAmount());
    assertEquals(date, unionCharge.getDate());
    assertEquals(savedCharge.getId(), unionCharge.getId());
  }
}
