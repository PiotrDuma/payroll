package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;

class ChangeHoldMethodTransaction extends AbstractChangeEmployeeTransaction
    implements ChangeEmployeeTransaction {
  private final PaymentMethodFactory methodFactory;
  public ChangeHoldMethodTransaction(EmployeeRepository repo, PaymentMethodFactory methodFactory,
      EmployeeId employeeId) {
    super(repo, employeeId);
    this.methodFactory = methodFactory;
  }

  @Override
  public Object execute() {
    Employee employee = getEmployee();
    PaymentMethod method = methodFactory.getHoldPaymentMethod();
    employee.setPaymentMethod(method);
    return getRepository().save(employee);
  }
}
