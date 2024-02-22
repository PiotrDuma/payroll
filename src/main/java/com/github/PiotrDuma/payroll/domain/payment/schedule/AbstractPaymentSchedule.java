package com.github.PiotrDuma.payroll.domain.payment.schedule;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import jakarta.persistence.Column;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

abstract class AbstractPaymentSchedule extends AbstractPaymentScheduleEntity {
  private static final String INVALID_DATE = "Provided date is invalid";

  protected AbstractPaymentSchedule() {
  }

  @Column(name = "next_payment_date")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate firstDayOfNextPaymentPeriod;

  protected AbstractPaymentSchedule(LocalDate created) {
    super();
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
