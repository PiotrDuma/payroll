package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;

class AddHourlyEmployeeTransaction extends AbstractAddEmployeeTransaction{
  private final HourlyClassification classification;
  private final PaymentScheduleFactory scheduleFactory;
  private final HourlyRate hourlyRate;

  public AddHourlyEmployeeTransaction(EmployeeRepository repository,
      HourlyClassification salariedClassification,
      PaymentScheduleFactory scheduleFactory,
      PaymentMethodFactory methodFactory,
      Address address, EmployeeName name, HourlyRate hourlyRate) {
    super(repository, methodFactory, address, name);
    this.classification = salariedClassification;
    this.scheduleFactory = scheduleFactory;
    this.hourlyRate = hourlyRate;
  }

  @Override
  public PaymentClassification getClassification() {
    return this.classification.getClassification(hourlyRate);
  }

  @Override
  public PaymentSchedule getPaymentSchedule() {
    return this.scheduleFactory.getWeeklySchedule();
  }
}