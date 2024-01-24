package com.github.PiotrDuma.payroll.domain.payment.classification;

import java.math.BigDecimal;

//TODO: validation requirements
public class HourlyRate {
  private BigDecimal hourlyRate;

  public HourlyRate(BigDecimal hourlyRate) {
    this.hourlyRate = hourlyRate;
  }

  public BigDecimal getHourlyRate() {
    return hourlyRate;
  }

  public void setHourlyRate(BigDecimal hourlyRate) {
    this.hourlyRate = hourlyRate;
  }
}
