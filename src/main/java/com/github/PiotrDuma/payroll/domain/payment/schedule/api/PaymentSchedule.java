package com.github.PiotrDuma.payroll.domain.payment.schedule.api;

import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentPeriod;
import java.time.LocalDate;

public interface PaymentSchedule {

  /**
   * @return checks if it's payment date
   */
  boolean isPayday(LocalDate today);

  /**
   * This method returns object describing payment period and sets the date of last payday to
   * provided argument {@code today}
   *
   * @param today the end of payment period
   * @return payment period since last payday
   */
  PaymentPeriod establishPaymentPeriod(LocalDate today);
}
