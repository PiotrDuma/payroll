package com.github.PiotrDuma.payroll.domain.payment.method;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class HoldPaymentMethodTest {
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
  void executePaymentShouldLog(){
    String expectedMessage1 = "Proceed hold payment method: salary provided to another department";
    HoldPaymentMethod holdPaymentMethod = new HoldPaymentMethod();
    LocalDate date = LocalDate.of(2000, 1, 1);
    Salary salary = new Salary(2000);

    holdPaymentMethod.executePayment(date, salary);

    int size = logWatcher.list.size();
    assertTrue(logWatcher.list.get(size - 1).getFormattedMessage().contains(expectedMessage1));
  }
}