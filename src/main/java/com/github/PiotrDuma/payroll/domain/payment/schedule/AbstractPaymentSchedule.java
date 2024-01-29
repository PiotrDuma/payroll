package com.github.PiotrDuma.payroll.domain.payment.schedule;

import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentPeriod;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import java.time.LocalDate;

abstract class AbstractPaymentSchedule implements PaymentSchedule {
  private static final String INVALID_DATE = "Provided date is invalid";
  private LocalDate lastPayDate;

  public AbstractPaymentSchedule(LocalDate created) {
    this.lastPayDate = created;
  }

  @Override
  public PaymentPeriod establishPaymentPeriod(LocalDate today) {
    checkDateValidation(today);
    PaymentPeriod period = new PaymentPeriod(this.lastPayDate.plusDays(1), today);
    this.lastPayDate = today;
    return period;
  }

  protected LocalDate getLastPayDate() {
    return lastPayDate;
  }

  protected void checkDateValidation(LocalDate today){
    if(today.isBefore(this.lastPayDate)){
      throw new IllegalArgumentException(INVALID_DATE);
    }
  }
}
