package com.github.PiotrDuma.payroll.domain.payment.schedule;

import java.time.LocalDate;

public class MonthlyPaymentSchedule extends AbstractPaymentSchedule{

  public MonthlyPaymentSchedule(LocalDate created) {
    super(created);
  }

  @Override
  public boolean isPayday(LocalDate today) {
    return today.getMonthValue()!=today.plusDays(1).getMonthValue();
  }
}
