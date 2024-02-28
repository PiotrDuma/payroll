package com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public class Hours {
  private static final String MIN_VALUE = "Number of hours cannot be lower than 0";
  private static final String MAX_VALUE = "Number of hours cannot be greater than 24";
  private static final String NULL_VALUE = "Number of hours cannot be empty";

  @NotNull(message = NULL_VALUE)
  @Min(value = 0, message = MIN_VALUE)
  @Max(value = 24, message = MAX_VALUE)
  private final Double hours;

  public Hours(Double hours) {
    this.hours = hours;
  }

  public Double getHours() {
    return hours;
  }

  @Override
  public int hashCode() {
    return Objects.hash(hours);
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof Hours o)){
      return false;
    }
    return o.getHours().equals(this.hours);
  }

  @Override
  public String toString() {
    return this.getHours().toString();
  }
}
