package com.github.PiotrDuma.payroll.domain.payment.schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;

class WeeklyPaymentSchedule extends AbstractPaymentSchedule {

  public WeeklyPaymentSchedule(LocalDate created) {
    super(created);
  }

  @Override
  public boolean isPayday(LocalDate today) {
    return today.getDayOfWeek().equals(DayOfWeek.FRIDAY);
  }
}
