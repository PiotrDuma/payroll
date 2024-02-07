package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.Amount;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
class UnionAffiliationServiceImpl implements UnionAffiliationService {
  private final UnionAffiliationTransactionFactory transactionFactory;

  public UnionAffiliationServiceImpl(UnionAffiliationTransactionFactory transactionFactory) {
    this.transactionFactory = transactionFactory;
  }

  @Override
  public UnionDto addUnion(String unionName) {
    return (UnionDto)transactionFactory
        .create(UnionTransaction.ADD_UNION, unionName)
        .execute();
  }

  @Override
  public void recordMembership(UUID unionId, EmployeeId employeeId) {
    transactionFactory.create(UnionTransaction.RECORD_MEMBERSHIP, unionId, employeeId)
        .execute();
  }

  @Override
  public void undoMembershipAffiliation(UUID unionId, EmployeeId employeeId) {
    transactionFactory.create(UnionTransaction.UNDO_MEMBERSHIP, unionId, employeeId)
        .execute();
  }

  @Override
  public void chargeMembers(UUID unionId, Amount amount, LocalDate date) {
    transactionFactory.create(UnionTransaction.CHARGE_MEMBERS, unionId, amount, date)
        .execute();
  }

  @Override
  public Amount countMembershipCharges(EmployeeId employeeId, PaymentPeriod period) {
    return (Amount)transactionFactory
        .create(UnionTransaction.CHARGE_MEMBERS, employeeId, period)
        .execute();
  }
}
