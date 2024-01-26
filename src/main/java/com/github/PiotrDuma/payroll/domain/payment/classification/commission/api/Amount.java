package com.github.PiotrDuma.payroll.domain.payment.classification.commission.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Amount{
  private static final String MIN_VALUE = "Amount cannot be lower than 0";
  private static final String NULL_VALUE = "Amount cannot empty";

  @NotNull(message = NULL_VALUE)
  @DecimalMin(value = "0.0", message = MIN_VALUE)
  private final BigDecimal value;

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

  public boolean equals(Amount obj) {
    return this.value.doubleValue()==obj.getAmount().doubleValue();
  }

  @Override
  public String toString() {
    return String.format(("%.2f"), this.value.doubleValue());
  }
}
