package com.github.PiotrDuma.payroll.domain.employee.api;

public class EmployeeName {
  private final String name;

  public EmployeeName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof EmployeeName o)){
      return false;
    }
    return o.getName().equals(name);
  }

  @Override
  public String toString() {
    return name;
  }
}
