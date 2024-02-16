package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.salary.api.SalariedClassification;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;

class ChangeSalariedClassificationTransaction extends AbstractChangeEmployeeTransaction
    implements ChangeEmployeeTransaction {
  private final SalariedClassification classification;
  private final PaymentScheduleFactory scheduleFactory;
  private final Salary salary;
  public ChangeSalariedClassificationTransaction(EmployeeRepository repo,
      SalariedClassification classification, PaymentScheduleFactory scheduleFactory,
      EmployeeId employeeId, Salary salary) {
    super(repo, employeeId);
    this.classification = classification;
    this.scheduleFactory = scheduleFactory;
    this.salary = salary;
  }

  @Override
  public Object execute() {
    Employee employee = getEmployee();
    PaymentClassification classification = this.classification.getClassification(salary);
    PaymentSchedule schedule = this.scheduleFactory.getMonthlySchedule();
    employee.setPaymentClassification(classification);
    employee.setSchedule(schedule);
    return getRepository().save(employee);
  }

  protected Salary getSalary() {
    return salary;
  }
}
