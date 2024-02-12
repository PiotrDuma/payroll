package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;

class ChangeDirectMethodTransaction extends AbstractChangeEmployeeTransaction
    implements ChangeEmployeeTransaction {
  private final PaymentMethodFactory methodFactory;
  private final Bank bank;
  private final BankAccount bankAccount;
  public ChangeDirectMethodTransaction(EmployeeRepository repo, PaymentMethodFactory methodFactory,
      EmployeeId employeeId, Bank bank, BankAccount bankAccount) {
    super(repo, employeeId);
    this.methodFactory = methodFactory;
    this.bank = bank;
    this.bankAccount = bankAccount;
  }

  @Override
  public Object execute() {
    Employee employee = getEmployee();
    PaymentMethod method = methodFactory.getDirectPaymentMethod(bank, bankAccount);
    employee.setPaymentMethod(method);
    return getRepository().save(employee);
  }

  protected Bank getBank() {
    return bank;
  }

  protected BankAccount getBankAccount() {
    return bankAccount;
  }
}
