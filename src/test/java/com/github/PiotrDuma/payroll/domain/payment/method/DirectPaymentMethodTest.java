package com.github.PiotrDuma.payroll.domain.payment.method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount.BankAccount;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentDto;
import java.time.LocalDate;
import java.util.UUID;
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
    LocalDate date = LocalDate.of(2000, 1, 1);
    Salary salary = new Salary(2000);

    paymentMethod.executePayment(date, salary);
    assertTrue(paymentMethod.getPayments().stream().findFirst().isPresent());
    UUID id = paymentMethod.getPayments().stream().findFirst().get().id();

    String expectedMessage2 = "Payment "+ id.toString() + " executed";

    int size = logWatcher.list.size();
    assertTrue(logWatcher.list.get(size - 2).getFormattedMessage().contains(expectedMessage1));
    assertTrue(logWatcher.list.get(size - 1).getFormattedMessage().contains(expectedMessage2));
  }

  @Test
  void executePaymentShouldAddNewPayment(){
    DirectPaymentMethod paymentMethod = new DirectPaymentMethod(BANK, BANK_ACCOUNT);
    LocalDate date = LocalDate.of(2000, 1, 1);
    Salary salary = new Salary(2000);
    paymentMethod.executePayment(date, salary);

    assertEquals(1, paymentMethod.getPayments().size());
    assertTrue(paymentMethod.getPayments().stream().findFirst().isPresent());
    PaymentDto payment = paymentMethod.getPayments().stream().findFirst().get();

    assertEquals(date, payment.date());
    assertEquals(salary.toString(), payment.salary().toString());
  }
}