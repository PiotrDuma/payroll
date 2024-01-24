package com.github.PiotrDuma.payroll.domain.payment.classification;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.schedule.PaymentPeriod;

public interface PaymentClassification {
  Salary calculatePay(PaymentPeriod paymentPeriod);
}
