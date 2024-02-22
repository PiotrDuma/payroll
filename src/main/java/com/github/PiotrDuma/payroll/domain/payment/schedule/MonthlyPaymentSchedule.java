package com.github.PiotrDuma.payroll.domain.payment.schedule;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("monthly")
class MonthlyPaymentSchedule extends AbstractPaymentSchedule{
  protected MonthlyPaymentSchedule() {
  }

  protected MonthlyPaymentSchedule(LocalDate created) {
    super(created);
  }

  @Override
  public boolean isPayday(LocalDate today) {
    return today.getMonthValue()!=today.plusDays(1).getMonthValue();
  }
}
