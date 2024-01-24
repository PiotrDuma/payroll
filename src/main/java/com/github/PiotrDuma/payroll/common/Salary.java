package com.github.PiotrDuma.payroll.common;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record Salary(
    @NotNull(message = NULL_VALUE) @DecimalMin(value = "0.0", message = MIN_VALUE) BigDecimal salary) {

  private static final String MIN_VALUE = "Salary cannot be lower than 0";
  private static final String NULL_VALUE = "Salary cannot empty";

  public Salary(BigDecimal salary) {
    this.salary = salary;
  }

  @Override
  public BigDecimal salary() {
    return salary;
  }
}
