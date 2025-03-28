package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UndoMembershipTransaction implements UnionTransaction {
  private static final Logger log = LoggerFactory.getLogger(UndoMembershipTransaction.class);

  private final UnionAffiliationRepository repository;
  private final UUID unionID;
  private final EmployeeId employeeId;

  public UndoMembershipTransaction(UnionAffiliationRepository repository, UUID unionID,
      EmployeeId employeeId) {
    this.repository = repository;
    this.unionID = unionID;
    this.employeeId = employeeId;
  }

  @Override
  public Object execute() {
    UnionEntity union = getUnion();
    checkEmployeeMembership(union);
    union.removeMembership(employeeId);
    repository.save(union);
    log.debug("Transaction executed: employee: " + employeeId + " removed from union: "+ unionID);
    return union;
  }

  private void checkEmployeeMembership(UnionEntity union) {
    if(!union.isMember(employeeId)){
      throw new InvalidArgumentException("Employee "+ employeeId + " is not affiliated with this union");
    }
  }

  private UnionEntity getUnion() {
    return this.repository.findById(unionID)
        .orElseThrow(() -> new ResourceNotFoundException("Union not found"));
  }
}
