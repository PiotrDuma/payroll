package com.github.PiotrDuma.payroll.common;

import java.util.UUID;

public class EmployeeId {
  private final UUID id;

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
}
