package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.amount.AmountConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "union_charge")
class UnionCharge {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @Convert(converter = AmountConverter.class)
  private Amount amount;
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate date;

  protected UnionCharge() {
  }

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

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof UnionCharge o)){
      return false;
    }
    return o.getId().equals(id) && o.getAmount().equals(amount) && o.getDate().equals(date);
  }
}
