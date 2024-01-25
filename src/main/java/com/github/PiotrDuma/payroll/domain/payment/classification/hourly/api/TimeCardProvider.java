package com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api;

import java.time.LocalDate;

public interface TimeCardProvider {
  void addOrUpdateTimeCard(LocalDate date, Hours hours);
}
