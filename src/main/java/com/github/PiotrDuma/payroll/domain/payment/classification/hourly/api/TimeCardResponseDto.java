package com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import java.time.LocalDate;

public class TimeCardResponseDto {
  private EmployeeId owner;
  private LocalDate date;
  private Hours hours;

  public TimeCardResponseDto() {
  }

  public TimeCardResponseDto(EmployeeId owner, LocalDate date, Hours hours) {
    this.owner = owner;
    this.date = date;
    this.hours = hours;
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

  public Hours getHours() {
    return hours;
  }

  public void setHours(Hours hours) {
    this.hours = hours;
  }
}
