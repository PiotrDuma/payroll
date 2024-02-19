package com.github.PiotrDuma.payroll.services.payday;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface PaycheckRepository {
  PaycheckEntity save(PaycheckEntity paycheck);
  List<PaycheckEntity> findAll();
  Optional<PaycheckEntity> findById(UUID id);
  List<PaycheckEntity> findAllByEmployeeId(EmployeeId employeeId);
}
