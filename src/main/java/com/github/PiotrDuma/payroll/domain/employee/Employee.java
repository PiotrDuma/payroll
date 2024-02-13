package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Employee implements EmployeeResponse {
  private static final Logger log = LoggerFactory.getLogger(Employee.class);
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
    this.schedule = null;
    this.paymentClassification = null;
    this.paymentMethod = null;
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
    if(this.schedule == null){
      log.error("Employee schedule reference: null");
      throw new RuntimeException("Missing employee's schedule method");
    }
    return schedule;
  }

  public void setSchedule(PaymentSchedule schedule) {
    this.schedule = schedule;
  }

  public PaymentClassification getPaymentClassification() {
    if(this.paymentClassification == null){
      log.error("Employee classification method reference: null");
      throw new RuntimeException("Missing employee's payment classification method");
    }
    return paymentClassification;
  }

  public void setPaymentClassification(PaymentClassification paymentClassification) {
    this.paymentClassification = paymentClassification;
  }

  public PaymentMethod getPaymentMethod() {
    if(this.paymentMethod == null){
      log.error("Employee payment method reference: null");
      throw new RuntimeException("Missing employee's payment method");
    }
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }
}
