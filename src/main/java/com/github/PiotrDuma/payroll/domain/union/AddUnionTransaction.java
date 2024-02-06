package com.github.PiotrDuma.payroll.domain.union;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AddUnionTransaction implements UnionTransaction{
  private static final Logger log = LoggerFactory.getLogger(AddUnionTransaction.class);
  private final UnionAffiliationRepository repository;
  private final String unionName;

  public AddUnionTransaction(UnionAffiliationRepository repository, String unionName) {
    this.repository = repository;
    this.unionName = unionName;
  }

  @Override
  public Object execute() {
    UnionEntity unionEntity = new UnionEntity(unionName);
    this.repository.save(unionEntity);
    log.debug("Transaction executed: add Union: id: "+ unionEntity.getId().toString());
    return unionEntity.toDto();
  }
}
