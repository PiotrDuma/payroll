package com.github.PiotrDuma.payroll.common.amount;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Amount{
  private static final String MIN_VALUE = "Amount cannot be lower than 0";
  private static final String NULL_VALUE = "Amount cannot empty";

  @NotNull(message = NULL_VALUE)
  @DecimalMin(value = "0.0", message = MIN_VALUE)
  private BigDecimal value;

  public Amount() {
  }

  public Amount(BigDecimal value) {
    this.value = value;
  }

  public Amount(Double value) {
    this.value = new BigDecimal(String.valueOf(value));
  }

  public Amount(int value) {
    this.value = new BigDecimal(String.valueOf(value));
  }

  public BigDecimal getAmount() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof Amount o)){
      return false;
    }
    return o.getAmount().compareTo(this.value) == 0;
  }

  @Override
  public String toString() {
    return String.format(("%.2f"), this.value.doubleValue());
  }
}
