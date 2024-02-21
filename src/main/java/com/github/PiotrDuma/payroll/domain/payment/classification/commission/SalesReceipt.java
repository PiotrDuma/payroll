package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.common.amount.AmountConverter;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeIdConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "sales_receipt")
class SalesReceipt {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @Column(name = "employee_id")
  @Convert(converter = EmployeeIdConverter.class)
  private EmployeeId employeeId;
  @Column(name = "date")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate date;
  @Column(name = "amount")
  @Convert(converter = AmountConverter.class)
  private Amount amount;

  public SalesReceipt() {
  }

  protected SalesReceipt(EmployeeId employeeId, LocalDate date, Amount amount) {
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
