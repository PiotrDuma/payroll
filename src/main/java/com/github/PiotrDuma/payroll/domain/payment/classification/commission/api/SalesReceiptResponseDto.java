package com.github.PiotrDuma.payroll.domain.payment.classification.commission.api;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import java.time.LocalDate;

public class SalesReceiptResponseDto {
  private EmployeeId owner;
  private LocalDate date;
  private Amount amount;

  public SalesReceiptResponseDto() {
  }

  public SalesReceiptResponseDto(EmployeeId owner, LocalDate date, Amount amount) {
    this.owner = owner;
    this.date = date;
    this.amount = amount;
  }

  public EmployeeId getOwner() {
    return owner;
  }

  public void setOwner(EmployeeId owner) {
    this.owner = owner;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Amount getAmount() {
    return amount;
  }

  public void setAmount(Amount amount) {
    this.amount = amount;
  }
}
