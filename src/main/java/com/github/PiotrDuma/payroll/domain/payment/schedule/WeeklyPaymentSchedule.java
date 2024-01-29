package com.github.PiotrDuma.payroll.domain.payment.schedule;

import java.time.LocalDate;

public class WeeklyPaymentSchedule extends AbstractPaymentSchedule {

  public WeeklyPaymentSchedule(LocalDate created) {
    super(created);
  }

  @Override
  public boolean isPayday(LocalDate today) {
    return false;
  }

  @Override
  public PaymentPeriod getPaymentPeriod(LocalDate today) {
    return null;
  }
}
