package com.github.PiotrDuma.payroll.domain.union;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UnionAffiliationRepositoryJpa extends JpaRepository<UnionEntity, UUID>,
    UnionAffiliationRepository {

}
