package com.github.PiotrDuma.payroll.domain.union;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class MockUnionAffiliationRepoImpl implements UnionAffiliationRepository{
  //TODO: REMOVE THIS AND IMPLEMENT INFRASTRUCTURE

  @Override
  public UnionEntity save(UnionEntity entity) {
    throw new RuntimeException("UnionAffiliationRepository class not implemented");
  }

  @Override
  public Optional<UnionEntity> findById(UUID unionId) {
    throw new RuntimeException("UnionAffiliationRepository class not implemented");
  }

  @Override
  public List<UnionEntity> findAll() {
    throw new RuntimeException("UnionAffiliationRepository class not implemented");
  }
}
