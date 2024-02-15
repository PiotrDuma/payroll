package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;

class ChangeAddressTransaction extends AbstractChangeEmployeeTransaction
    implements ChangeEmployeeTransaction{
  private final Address address;

  public ChangeAddressTransaction(EmployeeRepository repository, EmployeeId employeeId,
      Address address) {
    super(repository, employeeId);
    this.address = address;
  }

  @Override
  public Object execute() {
    Employee employee = super.getEmployee();
    employee.setAddress(this.address);
    return getRepository().save(employee);
  }

  protected Address getAddress() {
    return this.address;
  }
}
