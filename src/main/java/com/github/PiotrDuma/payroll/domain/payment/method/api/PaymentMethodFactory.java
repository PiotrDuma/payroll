package com.github.PiotrDuma.payroll.domain.payment.method.api;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;

public interface PaymentMethodFactory {
  PaymentMethod getHoldPaymentMethod();
  PaymentMethod getMailPaymentMethod(Address address);
  PaymentMethod getDirectPaymentMethod(Bank bank, BankAccount account);
}
