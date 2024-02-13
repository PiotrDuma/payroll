package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;

public interface AddEmployeeTransaction extends EmployeeTransaction {
  PaymentClassification getClassification();
  PaymentSchedule getPaymentSchedule();
}
