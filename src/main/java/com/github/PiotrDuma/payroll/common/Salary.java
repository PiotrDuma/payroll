package com.github.PiotrDuma.payroll.common;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Salary {
  private static final String MIN_VALUE = "Salary cannot be lower than 0";
  private static final String NULL_VALUE = "Salary cannot empty";

  @NotNull(message = NULL_VALUE)
  @DecimalMin(value = "0.0", message = MIN_VALUE)
  private final BigDecimal salary;

  public Salary(BigDecimal salary) {
    this.salary = salary;
  }

  public Salary(Double salary) {
    this.salary = new BigDecimal(String.valueOf(salary));
  }

  public Salary(int salary) {
    this.salary = new BigDecimal(String.valueOf(salary));
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public boolean equals(Salary obj) {
    return this.salary.doubleValue()==obj.getSalary().doubleValue();
  }

  @Override
  public String toString() {
    return String.format(("%.2f"), this.salary.doubleValue());
  }
}
