package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;

abstract class AbstractAddEmployeeTransaction implements AddEmployeeTransaction {
  protected final EmployeeRepository employeeRepository;
  protected final PaymentMethodFactory methodFactory;
  protected Address address;
  protected EmployeeName name;
  protected PaymentClassification classification;
  protected PaymentSchedule schedule;
  protected PaymentMethod method;

  protected AbstractAddEmployeeTransaction(EmployeeRepository employeeRepository,
      PaymentMethodFactory methodFactory,
      Address address, EmployeeName name) {
    this.employeeRepository = employeeRepository;
    this.methodFactory = methodFactory;
    this.address = address;
    this.name = name;
  }

  public abstract PaymentClassification getClassification();
  public abstract PaymentSchedule getPaymentSchedule();
  public void execute(){
    this.classification = getClassification();
    this.schedule = getPaymentSchedule();
    Employee employee = new Employee(name, address);
    employee.setPaymentClassification(this.classification);
    employee.setSchedule(this.schedule);
    employee.setPaymentMethod(methodFactory.getHoldPaymentMethod());
    this.employeeRepository.save(employee);
  }

  public Address getAddress() {
    return address;
  }

  public EmployeeName getName() {
    return name;
  }
}
