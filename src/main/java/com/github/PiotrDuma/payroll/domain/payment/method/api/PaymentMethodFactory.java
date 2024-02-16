package com.github.PiotrDuma.payroll.domain.payment.method.api;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount;

//TODO: consider to provide additional argument of EmployeeId for RESTController request variety
public interface PaymentMethodFactory {
  PaymentMethod getHoldPaymentMethod();
  PaymentMethod getMailPaymentMethod(Address address);
  PaymentMethod getDirectPaymentMethod(Bank bank, BankAccount account);
}
