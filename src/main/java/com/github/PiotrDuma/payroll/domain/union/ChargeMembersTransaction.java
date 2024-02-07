package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.Amount;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ChargeMembersTransaction implements UnionTransaction {
  private static final Logger log = LoggerFactory.getLogger(ChargeMembersTransaction.class);

  private final UnionAffiliationRepository repository;
  private final UUID unionID;
  private final Amount amount;
  private final LocalDate date;

  public ChargeMembersTransaction(UnionAffiliationRepository repository, UUID unionID,
      Amount amount, LocalDate date) {
    this.repository = repository;
    this.unionID = unionID;
    this.amount = amount;
    this.date = date;
  }

  @Override
  public Object execute() {
    UnionEntity union = getUnion();
    UnionCharge charge = union.addMembersCharge(amount, date);
    this.repository.save(union);
    return charge;
  }

  private UnionEntity getUnion() {
    return this.repository.findById(this.unionID)
        .orElseThrow(() -> new ResourceNotFoundException("Union not found"));
  }

  protected UUID getUnionID() {
    return unionID;
  }

  protected Amount getAmount() {
    return amount;
  }

  protected LocalDate getDate() {
    return date;
  }
}
