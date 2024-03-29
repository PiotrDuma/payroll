package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccountConverter;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bank.BankConverter;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "direct_payment")
class DirectPaymentMethod extends AbstractPaymentMethod implements PaymentMethod {
  private static final Logger log = LoggerFactory.getLogger(DirectPaymentMethod.class);
  @Column(name = "bank")
  @Convert(converter = BankConverter.class)
  private Bank bank;
  @Column(name = "account")
  @Convert(converter = BankAccountConverter.class)
  private BankAccount bankAccount;

  protected DirectPaymentMethod() {
  }

  protected DirectPaymentMethod(Bank bank, BankAccount bankAccount) {
    this.bank = bank;
    this.bankAccount = bankAccount;
  }

  @Override
  public void executePayment(Salary salary) {
    log.info("Proceed direct payment to "+ this.bank.toString() +
        " with account number: " + this.bankAccount.toString());
  }

  public Bank getBank() {
    return bank;
  }

  public BankAccount getBankAccount() {
    return bankAccount;
  }
}
