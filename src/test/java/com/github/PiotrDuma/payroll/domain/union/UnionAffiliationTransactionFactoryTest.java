package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.Amount;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnionAffiliationTransactionFactoryTest {

  @Mock
  private UnionAffiliationRepository repo;

  @InjectMocks
  private UnionAffiliationTransactionFactory factory;

  @Test
  void shouldReturnAddUnionTransaction(){
    String unionName = "UNION NAME";
    int transactionCode = UnionTransaction.ADD_UNION;
    UnionTransaction result = this.factory.create(transactionCode, unionName);

    assertTrue(result instanceof AddUnionTransaction);
    assertEquals(unionName, ((AddUnionTransaction) result).getUnionName());
  }

  @Test
  void shouldReturnRecordMembershipTransaction(){
    UUID unionID = UUID.randomUUID();
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    int transactionCode = UnionTransaction.RECORD_MEMBERSHIP;

    UnionTransaction result = this.factory.create(transactionCode, unionID, employeeId);

    assertTrue(result instanceof RecordMembershipTransaction);
    assertEquals(unionID, ((RecordMembershipTransaction) result).getUnionID());
    assertEquals(employeeId, ((RecordMembershipTransaction) result).getEmployeeId());
  }

  @Test
  void shouldReturnChargeMembersTransaction(){
    UUID unionID = UUID.randomUUID();
    Amount amount = new Amount(123);
    LocalDate date = LocalDate.of(2000, 1, 2);

    int transactionCode = UnionTransaction.CHARGE_MEMBERS;

    UnionTransaction result = this.factory.create(transactionCode, unionID, amount, date);

    assertTrue(result instanceof ChargeMembersTransaction);
    assertEquals(unionID, ((ChargeMembersTransaction) result).getUnionID());
    assertEquals(amount, ((ChargeMembersTransaction) result).getAmount());
    assertEquals(date, ((ChargeMembersTransaction) result).getDate());
  }
}