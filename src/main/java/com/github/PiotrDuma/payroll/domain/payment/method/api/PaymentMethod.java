package com.github.PiotrDuma.payroll.domain.payment.method.api;

import com.github.PiotrDuma.payroll.common.salary.Salary;
import java.time.LocalDate;

public interface PaymentMethod {

  /**
   * Method mocks operation of sending payment.
   * It only logs message. Implementation of payment transaction available.
   *
   * @param salary netto amount to be paid
   */
  void executePayment(Salary salary);
}
