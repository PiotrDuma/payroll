package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentDto;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DirectPaymentMethod implements PaymentMethod {
  Logger log = LoggerFactory.getLogger(DirectPaymentMethod.class);

  private UUID id;
  private Bank bank;
  private BankAccount bankAccount;
  private List<PaymentEntity> payments;

  public DirectPaymentMethod(Bank bank, BankAccount bankAccount) {
    this.id = UUID.randomUUID();
    this.bank = bank;
    this.bankAccount = bankAccount;
    this.payments = new LinkedList<>();
  }

  @Override
  public void executePayment(LocalDate date, Salary salary) {
    PaymentEntity payment = new PaymentEntity(date, salary);
    log.info("Proceed direct payment to "+ this.bank.toString() +
        " with account number: " + this.bankAccount.toString());
    this.payments.add(payment);
    log.info("Payment "+ payment.getId().toString() + " executed");
  }

  public List<PaymentDto> getPayments() {
    return payments.stream()
        .map(PaymentEntity::toPaymentDto)
        .collect(Collectors.toList());
  }

  public Bank getBank() {
    return bank;
  }

  public BankAccount getBankAccount() {
    return bankAccount;
  }
}
