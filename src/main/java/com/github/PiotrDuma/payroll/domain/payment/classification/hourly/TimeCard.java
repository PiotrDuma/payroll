package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeIdConverter;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "time_card")
class TimeCard {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @Column(name = "employee_id")
  @Convert(converter = EmployeeIdConverter.class)
  private EmployeeId employeeId; //foreign key just for requests
  @Column(name = "date")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
  private LocalDate date;
  @Column(name = "hours")
  @Convert(converter = HoursConverter.class)
  private Hours hours;

  protected TimeCard() {
  }

  protected TimeCard(EmployeeId employeeId,LocalDate date, Hours hours) {
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
