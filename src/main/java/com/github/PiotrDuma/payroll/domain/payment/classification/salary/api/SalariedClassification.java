package com.github.PiotrDuma.payroll.domain.payment.classification.salary.api;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;

public interface SalariedClassification extends PaymentClassification {
  PaymentClassification getClassification(Salary salary);
}
