package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;

class ChangeHourlyClassificationTransaction extends AbstractChangeEmployeeTransaction
    implements ChangeEmployeeTransaction {
  private final HourlyClassification hourlyClassification;
  private final PaymentScheduleFactory scheduleFactory;
  private final HourlyRate hourlyRate;

  public ChangeHourlyClassificationTransaction(EmployeeRepository repo,
      HourlyClassification hourlyClassification, PaymentScheduleFactory scheduleFactory,
      EmployeeId id, HourlyRate hourlyRate) {
    super(repo, id);
    this.hourlyClassification = hourlyClassification;
    this.scheduleFactory = scheduleFactory;
    this.hourlyRate = hourlyRate;
  }

  @Override
  public Object execute() {
    Employee employee = getEmployee();
    PaymentClassification classification = this.hourlyClassification.getClassification(hourlyRate);
    PaymentSchedule schedule = this.scheduleFactory.getWeeklySchedule();
    employee.setPaymentClassification(classification);
    employee.setSchedule(schedule);
    return getRepository().save(employee);
  }

  protected HourlyRate getHourlyRate() {
    return hourlyRate;
  }
}
