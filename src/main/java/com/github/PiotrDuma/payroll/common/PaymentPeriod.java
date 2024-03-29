package com.github.PiotrDuma.payroll.common;

import java.time.LocalDate;

public record PaymentPeriod(LocalDate startPeriod, LocalDate endPeriod) {

  private static final String MESSAGE = "Invalid time period";

  public PaymentPeriod {
    if (startPeriod.isAfter(endPeriod) || endPeriod.isBefore(startPeriod)) {
      throw new IllegalArgumentException(MESSAGE);
    }
  }
}
