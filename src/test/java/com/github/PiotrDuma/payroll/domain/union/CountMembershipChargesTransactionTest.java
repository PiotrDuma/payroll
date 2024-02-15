package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountMembershipChargesTransactionTest {
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());
  private static final LocalDate DATE = LocalDate.of(2000, 1, 2);
  private static final PaymentPeriod PERIOD = new PaymentPeriod(DATE, DATE.plusDays(3));
  private static final Amount AMOUNT = new Amount(123);

  @Mock
  private UnionAffiliationRepository repo;

  private CountMembershipChargesTransaction transaction;
  private UnionEntity union1;
  private UnionEntity union2;

  @BeforeEach
  void setUp(){
    this.union1 = new UnionEntity("name");
    this.union1.addMember(EMPLOYEE_ID);
    this.union2 = new UnionEntity("name2");

    this.union1.addMembersCharge(AMOUNT, DATE);
    this.union1.addMembersCharge(AMOUNT, DATE.plusDays(1));
    this.union1.addMembersCharge(AMOUNT, DATE.plusDays(7));
    this.union1.addMembersCharge(AMOUNT, DATE.minusDays(4));
    this.union2.addMembersCharge(AMOUNT, DATE);

    this.transaction = new CountMembershipChargesTransaction(repo, EMPLOYEE_ID, PERIOD);
    List<UnionEntity> findAll = List.of(union1, union2);
    when(this.repo.findAll()). thenReturn(findAll);
  }

  @Test
  void shouldCountValidChargesInPaymentPeriod(){
    Amount expected = new Amount(246); //two charges in period

    Amount result = (Amount) this.transaction.execute();

    assertEquals(expected.getAmount().doubleValue(), result.getAmount().doubleValue());
  }

  @Test
  void shouldCountValidChargesFromDifferentUnions(){
    this.union2.addMember(EMPLOYEE_ID);
    Amount expected = new Amount(369); //three charges in period

    Amount result = (Amount) this.transaction.execute();

    assertEquals(expected.getAmount().doubleValue(), result.getAmount().doubleValue());
  }

  @Test
  void shouldCountValidChargesWithDifferentPaymentPeriod(){
    PaymentPeriod newPeriod = new PaymentPeriod(DATE, DATE.plusDays(7));
    this.transaction = new CountMembershipChargesTransaction(repo, EMPLOYEE_ID, newPeriod);
    Amount expected = new Amount(369); //three charges in period

    Amount result = (Amount) this.transaction.execute();

    assertEquals(expected.getAmount().doubleValue(), result.getAmount().doubleValue());
  }
}