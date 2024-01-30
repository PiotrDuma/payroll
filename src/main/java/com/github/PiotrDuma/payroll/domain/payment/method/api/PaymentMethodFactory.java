package com.github.PiotrDuma.payroll.domain.payment.method.api;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount;
import com.github.PiotrDuma.payroll.domain.payment.method.PaymentMethod;

public interface PaymentMethodFactory {
  PaymentMethod getHoldPaymentMethod(Address address);
  PaymentMethod getMailPaymentMethod(Address address);
  PaymentMethod getDirectPaymentMethod(Bank bank, BankAccount account);
}
