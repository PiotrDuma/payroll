package com.github.PiotrDuma.payroll.services.payday;

import com.github.PiotrDuma.payroll.common.Amount;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.common.Salary;
import java.time.LocalDate;

class PaycheckEntity {
  private EmployeeId employeeId;
  private Salary salary;
  private Amount unionDues;
  private Salary netSalary;
  private LocalDate date;

  public PaycheckEntity(EmployeeId employeeId, Salary salary, Amount unionDues, LocalDate date) {
    this.employeeId = employeeId;
    this.salary = salary;
    this.unionDues = unionDues;
    this.netSalary = new Salary(salary.getSalary().subtract(unionDues.getAmount()));
  }

  public EmployeeId getEmployeeId() {
    return employeeId;
  }

  public Salary getSalary() {
    return salary;
  }

  public Amount getUnionDues() {
    return unionDues;
  }

  public Salary getNetSalary() {
    return netSalary;
  }

  public LocalDate getDate() {
    return date;
  }
}
