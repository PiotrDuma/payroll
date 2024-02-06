package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class UnionAffiliationTransactionFactory {
  private final UnionAffiliationRepository repository;

  @Autowired
  public UnionAffiliationTransactionFactory(UnionAffiliationRepository repository) {
    this.repository = repository;
  }

  public UnionTransaction create(int transactionCode, Object... params){
    return switch(transactionCode){
      case UnionTransaction.ADD_UNION -> new AddUnionTransaction(repository, (String)params[0]);
      case UnionTransaction.RECORD_MEMBERSHIP ->
          new RecordMembershipTransaction(repository, (UUID)params[0], (EmployeeId)params[1]);
      //TODO: extend by new transactions.
      default -> null;
    };
  }
}
