package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.Amount;
import java.time.LocalDate;
import java.util.UUID;

class SalesReceipt {
  private final UUID id;
  private EmployeeId employeeId;
  private LocalDate date;
  private Amount amount;

  public SalesReceipt(EmployeeId employeeId, LocalDate date, Amount amount) {
    this.id = UUID.randomUUID();
    this.employeeId = employeeId;
    this.date = date;
    this.amount = amount;
  }

  public LocalDate getDate() {
    return date;
  }

  public Amount getAmount() {
    return amount;
  }
}
