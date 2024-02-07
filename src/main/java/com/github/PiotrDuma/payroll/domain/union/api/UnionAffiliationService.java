package com.github.PiotrDuma.payroll.domain.union.api;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import java.util.UUID;

public interface UnionAffiliationService {
  UnionDto addUnion(String unionName);
  void recordMembership(UUID unionId, EmployeeId employeeId);
  void undoMembershipAffiliation(UUID unionId, EmployeeId employeeId);
}
