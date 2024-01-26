package com.github.PiotrDuma.payroll.domain.payment.classification.commission.api;

import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeId;
import java.time.LocalDate;

public interface SalesReceiptProvider {
  void addSalesReceipt(EmployeeId employeeId, LocalDate date, Amount amount);
}
