package com.github.PiotrDuma.payroll.domain.union;

import org.springframework.stereotype.Repository;

@Repository
interface UnionAffiliationRepository {
  UnionEntity save(UnionEntity entity);
}
