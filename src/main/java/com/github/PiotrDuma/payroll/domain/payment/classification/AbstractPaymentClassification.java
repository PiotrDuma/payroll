package com.github.PiotrDuma.payroll.domain.payment.classification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractPaymentClassification implements PaymentClassification{
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  public AbstractPaymentClassification() {
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }
}
