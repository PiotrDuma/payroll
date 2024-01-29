package com.github.PiotrDuma.payroll.domain.payment.schedule;

import java.time.LocalDate;

public interface PaymentSchedule {
  boolean isPayday(LocalDate today);
  PaymentPeriod getPaymentPeriod(LocalDate today);
}
