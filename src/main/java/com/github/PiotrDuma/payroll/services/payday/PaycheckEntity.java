package com.github.PiotrDuma.payroll.services.payday;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.amount.AmountConverter;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeIdConverter;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.common.salary.SalaryConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "paycheck")
class PaycheckEntity {
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @Column(name = "employee_id")
  @Convert(converter = EmployeeIdConverter.class)
  private EmployeeId employeeId;
  @Column(name = "salary")
  @Convert(converter = SalaryConverter.class)
  private Salary salary;
  @Column(name = "union_due")
  @Convert(converter = AmountConverter.class)
  private Amount unionDues;
  @Column(name = "net_salary")
  @Convert(converter = SalaryConverter.class)
  private Salary netSalary;
  @Column(name = "date")
  @DateTimeFormat(pattern = "yyyy/MM/dd")
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

  @Override
  public String toString() {
    return "PaycheckEntity{" +
        "id=" + id +
        ", employeeId=" + employeeId +
        ", salary=" + salary +
        ", unionDues=" + unionDues +
        ", netSalary=" + netSalary +
        ", date=" + date +
        '}';
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof PaycheckEntity o)){
      return false;
    }
    return o.getId().equals(id) &&
        o.getEmployeeId().equals(employeeId) &&
        o.getSalary().equals(salary) &&
        o.getUnionDues().equals(unionDues) &&
        o.getNetSalary().equals(netSalary) &&
        o.getDate().equals(date);
  }
}
