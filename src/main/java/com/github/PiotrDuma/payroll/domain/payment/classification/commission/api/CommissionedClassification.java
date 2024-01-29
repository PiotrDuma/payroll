package com.github.PiotrDuma.payroll.domain.payment.classification.commission.api;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;

public interface CommissionedClassification{
  PaymentClassification getClassification(Salary salary, CommissionRate commissionRate);
}
