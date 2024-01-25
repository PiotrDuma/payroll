package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import java.time.LocalDate;

class TimeCard {
  private LocalDate date;
  private Hours hours;

  public TimeCard(LocalDate date, Hours hours) {
    this.date = date;
    this.hours = hours;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Hours getHours() {
    return hours;
  }

  public void setHours(Hours hours) {
    this.hours = hours;
  }
}
