package com.github.PiotrDuma.payroll.domain.payment.schedule;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import java.time.LocalDate;

abstract class AbstractPaymentSchedule implements PaymentSchedule {
  private static final String INVALID_DATE = "Provided date is invalid";
  private LocalDate firstDayOfNextPaymentPeriod;

  public AbstractPaymentSchedule(LocalDate created) {
    this.firstDayOfNextPaymentPeriod = created;// if created today, should count that day
  }

  @Override
  public PaymentPeriod establishPaymentPeriod(LocalDate today) {
    checkDateValidation(today);
    PaymentPeriod period = new PaymentPeriod(this.firstDayOfNextPaymentPeriod, today);
    this.firstDayOfNextPaymentPeriod = today.plusDays(1);
    return period;
  }

  protected LocalDate getFirstDayOfNextPaymentPeriod() {
    return firstDayOfNextPaymentPeriod;
  }

  protected void checkDateValidation(LocalDate today){
    if(today.isBefore(this.firstDayOfNextPaymentPeriod)){
      throw new IllegalArgumentException(INVALID_DATE);
    }
  }
}
