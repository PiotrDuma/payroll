package com.github.PiotrDuma.payroll.domain.payment.classification.commission.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public class CommissionRate {
  private static final String NULL_VALUE_EXCEPTION = "Commission rate cannot be null";
  private static final String MIN_VALUE_EXCEPTION = "Commission rate cannot be lower than 0%";
  private static final String MAX_VALUE_EXCEPTION = "Commission rate cannot be greater than 100%";

  @NotNull(message = NULL_VALUE_EXCEPTION)
  @Min(value = 0, message = MIN_VALUE_EXCEPTION)
  @Max(value = 100, message = MAX_VALUE_EXCEPTION)
  private double commissionRate;

  public CommissionRate(double commissionRate) {
    this.commissionRate = commissionRate;
  }

  public double getCommissionRate() {
    return commissionRate;
  }

  public void setCommissionRate(double commissionRate) {
    this.commissionRate = commissionRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommissionRate that = (CommissionRate) o;
    return Double.compare(that.commissionRate, commissionRate) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(commissionRate);
  }
}
