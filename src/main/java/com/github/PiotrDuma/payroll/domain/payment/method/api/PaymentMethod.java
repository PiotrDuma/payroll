package com.github.PiotrDuma.payroll.domain.payment.method.api;

import com.github.PiotrDuma.payroll.common.salary.Salary;
import java.time.LocalDate;

/**
 * Interface extended by obligatory date
 *
 * TODO: consider implementing REST controller in future snapshot.
 */
public interface PaymentMethod {

  /**
   * Method mocks operation of sending payment.
   * It saves the payment and logging message.
   *
   * @param date current date
   * @param salary netto amount to be paid
   */
  void executePayment(LocalDate date, Salary salary);
}
