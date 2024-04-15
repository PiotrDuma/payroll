package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RecordMembershipTransaction implements UnionTransaction{
  private static final Logger log = LoggerFactory.getLogger(RecordMembershipTransaction.class);

  private final UnionAffiliationRepository repository;
  private final ReceiveEmployee employeeRepo;
  private final UUID unionID;
  private final EmployeeId employeeId;

  public RecordMembershipTransaction(UnionAffiliationRepository repository,
      ReceiveEmployee employeeRepo, UUID unionID, EmployeeId employeeId) {
    this.repository = repository;
    this.employeeRepo = employeeRepo;
    this.unionID = unionID;
    this.employeeId = employeeId;
  }

  @Override
  public Object execute() {
    checkMember(employeeId);
    UnionEntity union = getUnion();
    union.addMember(employeeId);
    repository.save(union);
    log.debug("Transaction executed: employee: " + employeeId + " recorded as member of union: "+ unionID);
    return union;
  }

  private UnionEntity getUnion() {
    return this.repository.findById(this.unionID)
        .orElseThrow(() -> new ResourceNotFoundException("Union not found"));
  }

  private void checkMember(EmployeeId employeeId){
    this.employeeRepo.find(employeeId);
  }

  protected UUID getUnionID() {
    return unionID;
  }

  protected EmployeeId getEmployeeId() {
    return employeeId;
  }
}
