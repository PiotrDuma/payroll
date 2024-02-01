package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import java.util.UUID;

class Employee {
  private final EmployeeId id;
  private EmployeeName name;
  private Address address;
  private PaymentSchedule schedule;
  private PaymentClassification paymentClassification;
  private PaymentMethod paymentMethod;

  public Employee(EmployeeName name, Address address) {
    this.id = new EmployeeId(UUID.randomUUID());
    this.name = name;
    this.address = address;
    this.schedule = null; //TODO: set system default strategy
    this.paymentClassification = null; //TODO: set system default strategy
    this.paymentMethod = null; //TODO: set system default strategy
  }

  public EmployeeId getId() {
    return id;
  }

  public EmployeeName getName() {
    return name;
  }

  public void setName(EmployeeName name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public PaymentSchedule getSchedule() {
    return schedule;
  }

  public void setSchedule(PaymentSchedule schedule) {
    this.schedule = schedule;
  }

  public PaymentClassification getPaymentClassification() {
    return paymentClassification;
  }

  public void setPaymentClassification(PaymentClassification paymentClassification) {
    this.paymentClassification = paymentClassification;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }
}
