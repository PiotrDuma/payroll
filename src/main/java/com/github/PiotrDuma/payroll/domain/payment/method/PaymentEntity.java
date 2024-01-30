package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentDto;
import java.time.LocalDate;
import java.util.UUID;

class PaymentEntity {
  private UUID id;
  private LocalDate date;
  private Salary salary;

  public PaymentEntity(LocalDate date, Salary salary) {
    this.id = UUID.randomUUID();
    this.date = date;
    this.salary = salary;
  }

  public UUID getId() {
    return id;
  }

  public LocalDate getDate() {
    return date;
  }

  public Salary getSalary() {
    return salary;
  }

  public PaymentDto toPaymentDto(){
    return new PaymentDto(this.id, this.date, this.salary);
  }
}
