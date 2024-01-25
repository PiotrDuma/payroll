package com.github.PiotrDuma.payroll.domain.payment.classification.salary;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.schedule.PaymentPeriod;

/**
 * Flat salary value, refactor if it should be calculated based on the days of payment period.
 */
class SalariedClassificationEntity implements PaymentClassification {
  private Salary salary;

  public SalariedClassificationEntity(Salary salary) {
    this.salary = salary;
  }

  @Override
  public Salary calculatePay(PaymentPeriod paymentPeriod) {
    return salary;
  }
}
