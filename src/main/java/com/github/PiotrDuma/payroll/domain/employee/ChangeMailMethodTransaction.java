package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;

class ChangeMailMethodTransaction extends AbstractChangeEmployeeTransaction
    implements ChangeEmployeeTransaction {
  private final PaymentMethodFactory methodFactory;
  private final Address address;
  public ChangeMailMethodTransaction(EmployeeRepository repo, PaymentMethodFactory methodFactory,
      EmployeeId employeeId, Address address) {
    super(repo, employeeId);
    this.methodFactory = methodFactory;
    this.address = address;
  }

  @Override
  public Object execute() {
    Employee employee = getEmployee();
    PaymentMethod method = methodFactory.getMailPaymentMethod(address);
    employee.setPaymentMethod(method);
    return getRepository().save(employee);
  }

  protected Address getAddress() {
    return address;
  }
}
