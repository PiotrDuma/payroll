package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import org.springframework.beans.factory.annotation.Autowired;

class ChangeNameTransaction extends AbstractChangeEmployeeTransaction
    implements ChangeEmployeeTransaction{
  private final EmployeeName name;

  @Autowired
  public ChangeNameTransaction(EmployeeRepository repository, EmployeeId employeeId, EmployeeName name) {
    super(repository, employeeId);
    this.name = name;
  }

  @Override
  public Object execute() {
    Employee employee = getEmployee();
    employee.setName(this.name);
    return getRepository().save(employee);
  }

  protected EmployeeName getName() {
    return name;
  }
}
