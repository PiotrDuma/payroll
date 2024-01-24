package com.github.PiotrDuma.payroll.domain.employee.api;

import java.util.UUID;

public class EmployeeId {
  private final UUID id;

  public EmployeeId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }
}
