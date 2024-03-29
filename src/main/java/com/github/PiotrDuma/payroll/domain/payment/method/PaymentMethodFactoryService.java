package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class PaymentMethodFactoryService implements PaymentMethodFactory {

  @Override
  public PaymentMethod getHoldPaymentMethod() {
    return new HoldPaymentMethod();
  }

  @Override
  public PaymentMethod getMailPaymentMethod(Address address) {
    return new MailPaymentMethod(address);
  }

  @Override
  public PaymentMethod getDirectPaymentMethod(Bank bank, BankAccount account) {
    return new DirectPaymentMethod(bank, account);
  }
}
