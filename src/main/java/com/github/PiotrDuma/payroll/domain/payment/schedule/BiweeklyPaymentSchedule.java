package com.github.PiotrDuma.payroll.domain.payment.schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;

class BiweeklyPaymentSchedule extends AbstractPaymentSchedule{
  public BiweeklyPaymentSchedule(LocalDate created) {
    super(created);
  }

  @Override
  public boolean isPayday(LocalDate today) {
    LocalDate lastPayDate = super.getFirstDayOfNextPaymentPeriod();
    return today.getDayOfWeek().equals(DayOfWeek.FRIDAY) &&
        !this.getFirstDayOfNextPaymentPeriod().plusDays(7).isAfter(today);
  }
}
