package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.PaymentMethod;

abstract class AbstractAddEmployeeTransaction implements AddEmployeeTransaction {
  protected final EmployeeRepository employeeRepository;
  protected Address address;
  protected EmployeeName name;
  protected PaymentClassification classification;
  protected PaymentSchedule schedule;
  protected PaymentMethod method;

  protected AbstractAddEmployeeTransaction(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public abstract Address getAddress();
  public abstract EmployeeName getName();
  public abstract PaymentClassification getClassification();
  public abstract PaymentSchedule getPaymentSchedule();
  public void execute(){
    this.classification = getClassification();
    this.schedule = getPaymentSchedule();
    Employee employee = new Employee(name, address);
    employee.setPaymentClassification(this.classification);
    employee.setSchedule(this.schedule);
    this.employeeRepository.save(employee);
  }
}
