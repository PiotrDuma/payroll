package com.github.PiotrDuma.payroll.domain.payment.schedule;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("biweekly")
class BiweeklyPaymentSchedule extends AbstractPaymentSchedule{
  protected BiweeklyPaymentSchedule() {
  }

  protected BiweeklyPaymentSchedule(LocalDate created) {
    super(created);
  }

  @Override
  public boolean isPayday(LocalDate today) {
    LocalDate lastPayDate = super.getFirstDayOfNextPaymentPeriod();
    return today.getDayOfWeek().equals(DayOfWeek.FRIDAY) &&
        !this.getFirstDayOfNextPaymentPeriod().plusDays(7).isAfter(today);
  }
}
