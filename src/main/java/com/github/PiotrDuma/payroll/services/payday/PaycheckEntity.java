package com.github.PiotrDuma.payroll.services.payday;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "paycheck")
class PaycheckEntity {
  @Id
  private UUID id;
  private EmployeeId employeeId;
  private Salary salary;
  private Amount unionDues;
  private Salary netSalary;
  private LocalDate date;

  protected PaycheckEntity(EmployeeId employeeId, Salary salary, Amount unionDues, LocalDate date) {
    this.id = UUID.randomUUID();
    this.employeeId = employeeId;
    this.salary = salary;
    this.unionDues = unionDues;
    this.date = date;
    this.netSalary = new Salary(salary.getSalary().subtract(unionDues.getAmount()));
  }

  protected PaycheckEntity() {
  }

  protected EmployeeId getEmployeeId() {
    return employeeId;
  }

  protected Salary getSalary() {
    return salary;
  }

  protected Amount getUnionDues() {
    return unionDues;
  }

  protected Salary getNetSalary() {
    return netSalary;
  }

  protected LocalDate getDate() {
    return date;
  }

  protected UUID getId() {
    return id;
  }
}
