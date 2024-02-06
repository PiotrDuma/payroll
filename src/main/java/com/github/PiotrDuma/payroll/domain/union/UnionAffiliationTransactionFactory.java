package com.github.PiotrDuma.payroll.domain.union;

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
      //TODO: extend by new transactions.
      default -> null;
    };
  }
}
