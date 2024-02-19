package com.github.PiotrDuma.payroll.services.payday;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface PaycheckRepositoryJpa extends PaycheckRepository, JpaRepository<PaycheckEntity, UUID> {

  @Override
  @Query(value = "SELECT p from PaycheckEntity p WHERE p.employeeId= :employeeId ORDER BY p.date DESC")
  List<PaycheckEntity> findAllByEmployeeId(@Param("employeeId") EmployeeId employeeId);
}
