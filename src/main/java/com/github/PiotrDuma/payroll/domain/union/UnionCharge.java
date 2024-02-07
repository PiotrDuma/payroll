package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.Amount;
import java.time.LocalDate;
import java.util.UUID;

class UnionCharge {
  private final UUID id;

  private final Amount amount;
  private final LocalDate date;

  protected UnionCharge(Amount amount, LocalDate date) {
    this.id = UUID.randomUUID();
    this.amount = amount;
    this.date = date;
  }

  protected UUID getId() {
    return id;
  }

  protected LocalDate getDate() {
    return date;
  }

  protected Amount getAmount() {
    return amount;
  }

  @Override
  public String toString() {
    return "UnionCharge{" +
        "id=" + id +
        ", date=" + date +
        ", amount=" + amount +
        '}';
  }
}
