package com.github.PiotrDuma.payroll.domain.payment.schedule;

import java.time.LocalDate;

abstract class AbstractPaymentSchedule implements PaymentSchedule{
  private LocalDate lastPayDate;

  public AbstractPaymentSchedule(LocalDate created) {
    this.lastPayDate = created;
  }

  protected void setLastPayDate(LocalDate lastPayDate) {
    this.lastPayDate = lastPayDate;
  }

  protected LocalDate getLastPayDate() {
    return this.lastPayDate;
  }
}
