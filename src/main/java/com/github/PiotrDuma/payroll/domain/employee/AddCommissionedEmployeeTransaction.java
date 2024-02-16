package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;

class AddCommissionedEmployeeTransaction extends AbstractAddEmployeeTransaction{
  private final CommissionedClassification classification;
  private final PaymentScheduleFactory scheduleFactory;
  private final Salary salary;
  private final CommissionRate commissionRate;

  public AddCommissionedEmployeeTransaction(EmployeeRepository repository,
      CommissionedClassification classification,
      PaymentScheduleFactory scheduleFactory,
      PaymentMethodFactory methodFactory,
      Address address, EmployeeName name, Salary salary, CommissionRate commissionRate) {
    super(repository, methodFactory, address, name);
    this.classification = classification;
    this.scheduleFactory = scheduleFactory;
    this.salary = salary;
    this.commissionRate = commissionRate;
  }

  @Override
  public PaymentClassification getClassification() {
    return this.classification.getClassification(salary, commissionRate);
  }

  @Override
  public PaymentSchedule getPaymentSchedule() {
    return this.scheduleFactory.getBiweeklySchedule();
  }
}
