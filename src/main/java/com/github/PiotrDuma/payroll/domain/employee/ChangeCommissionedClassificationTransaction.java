package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;

class ChangeCommissionedClassificationTransaction extends AbstractChangeEmployeeTransaction
    implements ChangeEmployeeTransaction {
  private final CommissionedClassification classification;
  private final PaymentScheduleFactory scheduleFactory;
  private final Salary salary;
  private final CommissionRate commissionRate;

  public ChangeCommissionedClassificationTransaction(EmployeeRepository repo,
      CommissionedClassification classification, PaymentScheduleFactory scheduleFactory,
      EmployeeId employeeId, Salary salary, CommissionRate rate) {
    super(repo, employeeId);
    this.classification = classification;
    this.scheduleFactory = scheduleFactory;
    this.salary = salary;
    this.commissionRate = rate;
  }

  @Override
  public Object execute() {
    Employee employee = getEmployee();
    PaymentClassification classification = this.classification.getClassification(salary, commissionRate);
    PaymentSchedule schedule = this.scheduleFactory.getBiweeklySchedule();
    employee.setPaymentClassification(classification);
    employee.setSchedule(schedule);
    return getRepository().save(employee);
  }

  protected Salary getSalary() {
    return salary;
  }

  protected CommissionRate getCommissionRate() {
    return commissionRate;
  }
}
