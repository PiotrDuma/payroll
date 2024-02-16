package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import org.springframework.stereotype.Service;

@Service
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
