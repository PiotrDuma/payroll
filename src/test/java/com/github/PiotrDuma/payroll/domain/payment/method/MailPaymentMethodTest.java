package com.github.PiotrDuma.payroll.domain.payment.method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentDto;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class MailPaymentMethodTest { //TODO: fix
  private static final Address ADDRESS = new Address("random address");

  private ListAppender<ILoggingEvent> logWatcher;

  @BeforeEach
  void setup() {
    logWatcher = new ListAppender<>();
    logWatcher.start();
    ((Logger) LoggerFactory.getLogger(MailPaymentMethod.class)).addAppender(logWatcher);
  }

  @AfterEach
  void teardown() {
    ((Logger) LoggerFactory.getLogger(MailPaymentMethod.class)).detachAndStopAllAppenders();
  }

  @Test
  void shouldInitObjectWithValidValues(){
    MailPaymentMethod paymentMethod = new MailPaymentMethod(ADDRESS);

    assertEquals(ADDRESS, paymentMethod.getAddress());
  }

//  @Test
//  void executePaymentShouldLog(){
//    String expectedMessage1 = "Proceed mail payment with address: " + ADDRESS.toString();
//    MailPaymentMethod paymentMethod = new MailPaymentMethod(ADDRESS);
//    LocalDate date = LocalDate.of(2000, 1, 1);
//    Salary salary = new Salary(2000);
//
//    paymentMethod.executePayment(date, salary);
//    assertTrue(paymentMethod.getPayments().stream().findFirst().isPresent());
//    UUID id = paymentMethod.getPayments().stream().findFirst().get().id();
//
//    String expectedMessage2 = "Payment "+ id.toString() + " executed";
//
//    int size = logWatcher.list.size();
//    assertTrue(logWatcher.list.get(size - 2).getFormattedMessage().contains(expectedMessage1));
//    assertTrue(logWatcher.list.get(size - 1).getFormattedMessage().contains(expectedMessage2));
//  }
//
//  @Test
//  void executePaymentShouldAddNewPayment(){
//    MailPaymentMethod paymentMethod = new MailPaymentMethod(ADDRESS);
//    LocalDate date = LocalDate.of(2000, 1, 1);
//    Salary salary = new Salary(2000);
//    paymentMethod.executePayment(date, salary);
//
//    assertEquals(1, paymentMethod.getPayments().size());
//    assertTrue(paymentMethod.getPayments().stream().findFirst().isPresent());
//    PaymentDto payment = paymentMethod.getPayments().stream().findFirst().get();
//
//    assertEquals(date, payment.date());
//    assertEquals(salary.toString(), payment.salary().toString());
//  }
}