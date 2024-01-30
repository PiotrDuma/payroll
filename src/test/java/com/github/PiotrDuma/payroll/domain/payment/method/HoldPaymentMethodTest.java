package com.github.PiotrDuma.payroll.domain.payment.method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentDto;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class HoldPaymentMethodTest {
  private static final Address ADDRESS = new Address("random address");

  private ListAppender<ILoggingEvent> logWatcher;

  @BeforeEach
  void setup() {
    logWatcher = new ListAppender<>();
    logWatcher.start();
    ((Logger) LoggerFactory.getLogger(HoldPaymentMethod.class)).addAppender(logWatcher);
  }

  @AfterEach
  void teardown() {
    ((Logger) LoggerFactory.getLogger(HoldPaymentMethod.class)).detachAndStopAllAppenders();
  }

  @Test
  void shouldInitObjectWithValidValues(){
    HoldPaymentMethod holdPaymentMethod = new HoldPaymentMethod(ADDRESS);

    assertEquals(ADDRESS, holdPaymentMethod.getAddress());
  }

  @Test
  void executePaymentShouldLog(){
    String expectedMessage1 = "Proceed hold payment with address: random address";
    HoldPaymentMethod holdPaymentMethod = new HoldPaymentMethod(ADDRESS);
    LocalDate date = LocalDate.of(2000, 1, 1);
    Salary salary = new Salary(2000);
    holdPaymentMethod.executePayment(date, salary);
    assertTrue(holdPaymentMethod.getPayments().stream().findFirst().isPresent());
    UUID id = holdPaymentMethod.getPayments().stream().findFirst().get().id();

    String expectedMessage2 = "Payment "+ id.toString() + " executed";

    int size = logWatcher.list.size();
    assertTrue(logWatcher.list.get(size - 2).getFormattedMessage().contains(expectedMessage1));
    assertTrue(logWatcher.list.get(size - 1).getFormattedMessage().contains(expectedMessage2));
  }

  @Test
  void executePaymentShouldAddNewPayment(){
    HoldPaymentMethod holdPaymentMethod = new HoldPaymentMethod(ADDRESS);
    LocalDate date = LocalDate.of(2000, 1, 1);
    Salary salary = new Salary(2000);
    holdPaymentMethod.executePayment(date, salary);

    assertEquals(1, holdPaymentMethod.getPayments().size());
    assertTrue(holdPaymentMethod.getPayments().stream().findFirst().isPresent());
    PaymentDto payment = holdPaymentMethod.getPayments().stream().findFirst().get();

    assertEquals(date, payment.date());
    assertEquals(salary.toString(), payment.salary().toString());
  }
}