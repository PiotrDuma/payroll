package com.github.PiotrDuma.payroll.domain.payment.schedule;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("weekly")
class WeeklyPaymentSchedule extends AbstractPaymentSchedule {
  protected WeeklyPaymentSchedule() {
  }

  protected WeeklyPaymentSchedule(LocalDate created) {
    super(created);
  }

  @Override
  public boolean isPayday(LocalDate today) {
    return today.getDayOfWeek().equals(DayOfWeek.FRIDAY);
  }
}
