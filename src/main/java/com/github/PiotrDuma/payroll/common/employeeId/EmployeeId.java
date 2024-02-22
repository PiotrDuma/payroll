package com.github.PiotrDuma.payroll.common.employeeId;

import java.io.Serializable;
import java.util.UUID;

public class EmployeeId implements Serializable {
  private UUID id;

  public EmployeeId() {
  }

  public EmployeeId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  @Override
  public String toString() {
    return this.id.toString();
  }

//  @Override
//  public int hashCode() {
//    return super.hashCode();
//  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof EmployeeId o)){
      return false;
    }
    return o.getId().equals(this.id);
  }
}
