package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.salary.api.SalariedClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class AddEmployeeTransactionFactoryService implements AddEmployeeTransactionFactory {
  private final EmployeeRepository repository;
  private final SalariedClassification salariedClassification;
  private final CommissionedClassification commissionedClassification;
  private final HourlyClassification hourlyClassification;
  private final PaymentScheduleFactory scheduleFactory;
  private final PaymentMethodFactory methodFactory;

  @Autowired
  public AddEmployeeTransactionFactoryService(EmployeeRepository repository,
      SalariedClassification salariedClassification,
      CommissionedClassification commissionedClassification,
      HourlyClassification hourlyClassification,
      PaymentScheduleFactory scheduleFactory,
      PaymentMethodFactory methodFactory) {
    this.repository = repository;
    this.salariedClassification = salariedClassification;
    this.commissionedClassification = commissionedClassification;
    this.hourlyClassification = hourlyClassification;
    this.scheduleFactory = scheduleFactory;
    this.methodFactory = methodFactory;
  }

  @Override
  public AddEmployeeTransaction initSalariedEmployeeTransaction(Address address, EmployeeName name,
      Salary salary) {
    return new AddSalariedEmployeeTransaction(repository, salariedClassification, scheduleFactory,
        methodFactory, address, name, salary);
  }

  @Override
  public AddEmployeeTransaction initHourlyEmployeeTransaction(Address address, EmployeeName name,
      HourlyRate hourlyRate) {
    return new AddHourlyEmployeeTransaction(repository, hourlyClassification, scheduleFactory,
        methodFactory, address, name, hourlyRate);
  }

  @Override
  public AddEmployeeTransaction initSalariedEmployeeTransaction(Address address, EmployeeName name,
      Salary salary, CommissionRate commissionRate) {
    return new AddCommissionedEmployeeTransaction(repository, commissionedClassification,
        scheduleFactory, methodFactory, address, name, salary, commissionRate);
  }
}
