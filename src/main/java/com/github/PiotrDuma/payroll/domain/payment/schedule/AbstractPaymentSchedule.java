package com.github.PiotrDuma.payroll.domain.payment.schedule;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

//@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
abstract class AbstractPaymentSchedule extends AbstractPaymentScheduleEntity implements PaymentSchedule {
  private static final String INVALID_DATE = "Provided date is invalid";
  @Column(name = "next_payment_date")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate firstDayOfNextPaymentPeriod;

  public AbstractPaymentSchedule(LocalDate created) {
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
