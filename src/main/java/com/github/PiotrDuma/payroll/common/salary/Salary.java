package com.github.PiotrDuma.payroll.common.salary;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Salary {
  private static final String MIN_VALUE = "Salary cannot be lower than 0";
  private static final String NULL_VALUE = "Salary cannot empty";

  @NotNull(message = NULL_VALUE)
  @DecimalMin(value = "0.0", message = MIN_VALUE)
  private BigDecimal salary;

  public Salary() {
  }

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

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof Salary o)){
      return false;
    }
    return o.getSalary().compareTo(this.salary) == 0;
  }

  @Override
  public String toString() {
    return String.format(("%.2f"), this.salary.doubleValue());
  }
}
