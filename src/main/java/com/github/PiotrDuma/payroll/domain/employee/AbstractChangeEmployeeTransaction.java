package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.Optional;

abstract class AbstractChangeEmployeeTransaction {
  private final EmployeeRepository repository;
  private final EmployeeId employeeId;

  public AbstractChangeEmployeeTransaction(EmployeeRepository repository, EmployeeId employeeId) {
    this.repository = repository;
    this.employeeId = employeeId;
  }

  protected Employee getEmployee() {
    Optional<Employee> employee = this.repository.findById(employeeId);
    if(employee.isEmpty()){
      throw new ResourceNotFoundException("Employee with id: " + employeeId + " not found");
    }
    return employee.get();
  }

  protected EmployeeRepository getRepository(){
    return this.repository;
  }

  protected EmployeeId getEmployeeId() {
    return employeeId;
  }
}
