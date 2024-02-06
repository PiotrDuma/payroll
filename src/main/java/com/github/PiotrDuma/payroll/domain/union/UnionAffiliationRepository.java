package com.github.PiotrDuma.payroll.domain.union;

import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
interface UnionAffiliationRepository {
  UnionEntity save(UnionEntity entity); //TODO: check if repository impl updates entity.
  UnionEntity findById(UUID unionId);
}
