package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.salary.api.SalariedClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;

class AddSalariedEmployeeTransaction extends AbstractAddEmployeeTransaction{
  private final SalariedClassification classification;
  private final PaymentScheduleFactory scheduleFactory;
  private final Salary salary;

  public AddSalariedEmployeeTransaction(EmployeeRepository repository,
        SalariedClassification salariedClassification,
        PaymentScheduleFactory scheduleFactory,
        PaymentMethodFactory methodFactory,
        Address address, EmployeeName name, Salary salary) {
    super(repository, methodFactory, address, name);
    this.classification = salariedClassification;
    this.scheduleFactory = scheduleFactory;
    this.salary = salary;
  }

  @Override
  public PaymentClassification getClassification() {
    return this.classification.getClassification(salary);
  }

  @Override
  public PaymentSchedule getPaymentSchedule() {
    return this.scheduleFactory.getMonthlySchedule();
  }
}
