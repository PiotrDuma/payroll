package com.github.PiotrDuma.payroll.domain.payment.method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class DirectPaymentMethodTest {
  private static final Bank BANK = new Bank("BANK NAME");
  private static final BankAccount BANK_ACCOUNT = new BankAccount("1234567890123456789012345");

  private ListAppender<ILoggingEvent> logWatcher;

  @BeforeEach
  void setup() {
    logWatcher = new ListAppender<>();
    logWatcher.start();
    ((Logger) LoggerFactory.getLogger(DirectPaymentMethod.class)).addAppender(logWatcher);
  }

  @AfterEach
  void teardown() {
    ((Logger) LoggerFactory.getLogger(DirectPaymentMethod.class)).detachAndStopAllAppenders();
  }

  @Test
  void shouldInitObjectWithValidValues(){
    DirectPaymentMethod paymentMethod = new DirectPaymentMethod(BANK, BANK_ACCOUNT);

    assertEquals(BANK.toString(), paymentMethod.getBank().toString());
    assertEquals(BANK_ACCOUNT.toString(), paymentMethod.getBankAccount().toString());
  }

  @Test
  void executePaymentShouldLog(){
    String expectedMessage1 = "Proceed direct payment to "+ BANK.toString() +
        " with account number: " + BANK_ACCOUNT.toString();
    DirectPaymentMethod paymentMethod = new DirectPaymentMethod(BANK, BANK_ACCOUNT);
    Salary salary = new Salary(2000);

    paymentMethod.executePayment(salary);

    int size = logWatcher.list.size();
    assertTrue(logWatcher.list.get(size - 1).getFormattedMessage().contains(expectedMessage1));
  }
}