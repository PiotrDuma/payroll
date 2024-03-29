package com.github.PiotrDuma.payroll.domain.union.api;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import java.time.LocalDate;
import java.util.UUID;

public interface UnionAffiliationService {
  UnionDto addUnion(String unionName);
  void recordMembership(UUID unionId, EmployeeId employeeId);
  void undoMembershipAffiliation(UUID unionId, EmployeeId employeeId);
  void chargeMembers(UUID unionId, Amount amount, LocalDate date);
  Amount countMembershipCharges(EmployeeId employeeId, PaymentPeriod period);
}
