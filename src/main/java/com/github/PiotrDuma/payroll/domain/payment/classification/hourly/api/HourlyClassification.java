package com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api;

import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import java.time.LocalDate;

public interface HourlyClassification {
  PaymentClassification getClassification(HourlyRate hourlyRate);
}
