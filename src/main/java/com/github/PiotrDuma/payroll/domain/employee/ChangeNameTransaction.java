package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

class ChangeNameTransaction implements ChangeEmployeeTransaction{
  private final EmployeeRepository repository;
  private final EmployeeId id;
  private final EmployeeName name;

  @Autowired
  public ChangeNameTransaction(EmployeeRepository repository, EmployeeId employeeId, EmployeeName name) {
    this.repository = repository;
    this.id = employeeId;
    this.name = name;
  }

  @Override
  public Object execute() {
    Employee employee = getEmployee();
    employee.setName(this.name);
    return this.repository.save(employee);
  }

  private Employee getEmployee() {
    Optional<Employee> employee = this.repository.findById(id);
    if(employee.isEmpty()){
      throw new ResourceNotFoundException("Employee with id: " + id + " not found");
    }
    return employee.get();
  }


}
