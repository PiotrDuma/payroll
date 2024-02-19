package com.github.PiotrDuma.payroll.services.payday;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PaycheckRepositoryJpa extends PaycheckRepository, JpaRepository<PaycheckEntity, UUID> {

}
