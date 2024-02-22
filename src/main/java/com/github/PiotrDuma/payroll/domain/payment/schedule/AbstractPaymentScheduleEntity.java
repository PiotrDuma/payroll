package com.github.PiotrDuma.payroll.domain.payment.schedule;

import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "method")
public abstract class AbstractPaymentScheduleEntity implements PaymentSchedule {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  protected AbstractPaymentScheduleEntity() {
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }
}
