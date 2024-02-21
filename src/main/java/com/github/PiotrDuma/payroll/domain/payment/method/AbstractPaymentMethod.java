package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractPaymentMethod implements PaymentMethod {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  public AbstractPaymentMethod() {
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }
}
