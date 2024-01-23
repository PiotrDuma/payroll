package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.domain.employee.schedule.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.employee.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.services.transaction.Transaction;

public interface AddEmployeeTransaction extends Transaction {
  PaymentClassification getClassification();
  PaymentSchedule getPaymentSchedule();
}
