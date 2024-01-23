package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.domain.employee.schedule.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.classification.PaymentClassification;

abstract class AbstractAddEmployeeTransaction implements AddEmployeeTransaction {
  protected final EmployeeRepository employeeRepository;
//  protected EmployeeId empID; //TODO: field setters or builder pattern.
//  protected Address itsAddress;
//  protected EmployeeName itsName;

  protected AbstractAddEmployeeTransaction(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public abstract PaymentClassification getClassification();
  public abstract PaymentSchedule getPaymentSchedule();
  public void execute(){
    //TODO: implement method.
  };
}
