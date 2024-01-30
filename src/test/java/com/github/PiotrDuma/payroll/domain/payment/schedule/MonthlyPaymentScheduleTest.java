package com.github.PiotrDuma.payroll.domain.payment.schedule;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentPeriod;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class MonthlyPaymentScheduleTest {
  private static final LocalDate CREATED = LocalDate.of(2000, 1,1);

  @Test
  void isPayDayShouldReturnTrueIfItsLayDayOfTheMonth(){
    LocalDate lastDayOfMonth = LocalDate.of(2000, 1,31);
    PaymentSchedule schedule = new MonthlyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(lastDayOfMonth));
  }

  @Test
  void isPayDayShouldReturnFalseIfItsNotLayDayOfTheMonth(){
    LocalDate lastDayOfMonth = LocalDate.of(2000, 1,31);
    PaymentSchedule schedule = new MonthlyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(lastDayOfMonth));
  }

  @Test
  void shouldReturnValidPaymentPeriodWhenCreatedFirstDayOfAMonth(){
    LocalDate lastDayOfMonth = LocalDate.of(2000, 1,31);
    PaymentPeriod expected = new PaymentPeriod(CREATED, lastDayOfMonth);
    MonthlyPaymentSchedule schedule = new MonthlyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(lastDayOfMonth));
    PaymentPeriod result = schedule.establishPaymentPeriod(lastDayOfMonth);

    assertEquals(expected, result);
    assertEquals(lastDayOfMonth.plusDays(1), schedule.getFirstDayOfNextPaymentPeriod());
  }

  @Test
  void shouldReturnValidPaymentPeriodWhenCreatedLastDayOfAMonth(){
    LocalDate lastDayOfMonth = CREATED.minusDays(1);
    PaymentPeriod expected = new PaymentPeriod(lastDayOfMonth, lastDayOfMonth);
    MonthlyPaymentSchedule schedule = new MonthlyPaymentSchedule(lastDayOfMonth);

    assertTrue(schedule.isPayday(lastDayOfMonth));
    PaymentPeriod result = schedule.establishPaymentPeriod(lastDayOfMonth);

    assertEquals(expected, result);
    assertEquals(lastDayOfMonth.plusDays(1), schedule.getFirstDayOfNextPaymentPeriod());
  }
}