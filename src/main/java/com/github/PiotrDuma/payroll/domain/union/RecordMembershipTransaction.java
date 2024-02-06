package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordMembershipTransaction implements UnionTransaction{
  private static final Logger log = LoggerFactory.getLogger(RecordMembershipTransaction.class);

  private final UnionAffiliationRepository repository;
  private final UUID unionID;
  private final EmployeeId employeeId;

  public RecordMembershipTransaction(UnionAffiliationRepository repository, UUID unionID,
      EmployeeId employeeId) {
    this.repository = repository;
    this.unionID = unionID;
    this.employeeId = employeeId;
  }

  @Override
  public Object execute() {
    UnionEntity union = this.repository.findById(this.unionID);
    union.addMember(employeeId);
    repository.save(union);
    log.debug("Transaction executed: employee: " + employeeId + " recorded as member of union: "+ unionID);
    return union;
  }

  protected UUID getUnionID() {
    return unionID;
  }

  protected EmployeeId getEmployeeId() {
    return employeeId;
  }
}
