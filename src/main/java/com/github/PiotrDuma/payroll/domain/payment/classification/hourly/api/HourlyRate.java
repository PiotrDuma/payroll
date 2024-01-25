package com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Allow work for free. May be changed to minimum wage.
 */
public class HourlyRate{

  private static final String MINIMUM_VALUE = "Hourly rate cannot be lower than 0";
  private static final String NULL_VALUE = "Hourly rate cannot be empty";

  @NotNull(message = NULL_VALUE)
  @DecimalMin(value = "0.0", message = MINIMUM_VALUE)
  private final BigDecimal hourlyRate;

  public HourlyRate(BigDecimal hourlyRate) {
    this.hourlyRate = hourlyRate;
  }

  public HourlyRate(Double hourlyRate) {
    this.hourlyRate = new BigDecimal(String.valueOf(hourlyRate));
  }

  public HourlyRate(int hourlyRate) {
    this.hourlyRate = new BigDecimal(String.valueOf(hourlyRate));
  }

  public BigDecimal getHourlyRate() {
    return hourlyRate;
  }

  public boolean equals(HourlyRate o) {
    return this.getHourlyRate().doubleValue() == o.getHourlyRate().doubleValue();
  }
}
