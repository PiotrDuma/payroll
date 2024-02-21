package com.github.PiotrDuma.payroll.domain.payment.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class AbstractPaymentScheduleEntity {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  public AbstractPaymentScheduleEntity() {
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }
}
