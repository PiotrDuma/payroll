package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
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
}
