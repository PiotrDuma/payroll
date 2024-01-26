package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import java.time.LocalDate;

class TimeCard {
  private EmployeeId employeeId; //foreign key just for requests
  private LocalDate date;
  private Hours hours;

  public TimeCard(EmployeeId employeeId,LocalDate date, Hours hours) {
    this.employeeId = employeeId;
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

  public EmployeeId getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(EmployeeId employeeId) {
    this.employeeId = employeeId;
  }
}
