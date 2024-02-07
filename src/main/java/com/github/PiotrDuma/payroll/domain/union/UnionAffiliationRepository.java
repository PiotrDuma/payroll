package com.github.PiotrDuma.payroll.domain.union;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
interface UnionAffiliationRepository {
  UnionEntity save(UnionEntity entity); //TODO: check if repository impl updates entity.
  Optional<UnionEntity> findById(UUID unionId);
  List<UnionEntity> findAll();
}
