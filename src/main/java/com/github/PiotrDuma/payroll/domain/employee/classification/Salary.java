package com.github.PiotrDuma.payroll.domain.employee.classification;

import java.math.BigDecimal;

//TODO: validation requirements
public class Salary {
  private BigDecimal salary;

  public Salary(BigDecimal salary) {
    this.salary = salary;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }
}
