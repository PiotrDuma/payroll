package com.github.PiotrDuma.payroll.domain.payment.schedule;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class BiweeklyPaymentScheduleTest {
  private static final LocalDate CREATED = LocalDate.of(1999, 12,30);

  @Test
  void shouldReturnFalseIfItsNotFriday(){
    PaymentSchedule schedule = new BiweeklyPaymentSchedule(CREATED); //thursday

    assertFalse(schedule.isPayday(CREATED));
  }

  @Test
  void shouldReturnFalseIfItsFirstFriday(){
    LocalDate firstFriday = CREATED.plusDays(1);
    PaymentSchedule schedule = new BiweeklyPaymentSchedule(CREATED);

    assertFalse(schedule.isPayday(firstFriday));
  }

  @Test
  void shouldReturnTrueIfItsSecondFriday(){
    LocalDate secondFriday = CREATED.plusDays(8); //second friday since created.
    PaymentSchedule schedule = new BiweeklyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(secondFriday));
  }

  @Test
  void shouldReturnTrueIfCreatedLastFriday(){
    LocalDate secondFriday = CREATED.plusDays(8);
    PaymentSchedule schedule = new BiweeklyPaymentSchedule(CREATED.plusDays(1));

    assertTrue(schedule.isPayday(secondFriday));
  }

  @Test
  void shouldReturnTrueTwoWeeksAfterLastPayment(){
    LocalDate secondFriday = CREATED.plusDays(8); //second friday since created: 2000-01-07
    PaymentSchedule schedule = new BiweeklyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(secondFriday));
    schedule.establishPaymentPeriod(secondFriday);
    assertFalse(schedule.isPayday(secondFriday.plusDays(7))); //2000-01-14
    assertTrue(schedule.isPayday(secondFriday.plusDays(14))); //2000-01-21
  }

  @Test
  void shouldReturnValidDatePeriod(){
    LocalDate secondFriday = CREATED.plusDays(8); //PAYDAY: 2000-01-07, friday
    PaymentPeriod expected = new PaymentPeriod(CREATED, secondFriday); //1999-12-30 - 2000-01-07
    BiweeklyPaymentSchedule schedule = new BiweeklyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(secondFriday));
    PaymentPeriod result = schedule.establishPaymentPeriod(secondFriday);

    assertEquals(expected, result);
    assertEquals(secondFriday.plusDays(1), schedule.getFirstDayOfNextPaymentPeriod());
  }

  @Test
  void shouldReturnValidDatePeriodTwoWeeksAfterLastPaymentPeriod(){
    LocalDate secondFriday = CREATED.plusDays(8); //PAYDAY: 2000-01-07, friday
    LocalDate nextPayday = secondFriday.plusDays(14); //PAYDAY: 2000-01-21, friday

    PaymentPeriod expected = new PaymentPeriod(secondFriday.plusDays(1), nextPayday); //1999-12-30 - 2000-01-07
    BiweeklyPaymentSchedule schedule = new BiweeklyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(secondFriday));
    schedule.establishPaymentPeriod(secondFriday); //first period
    assertFalse(schedule.isPayday(secondFriday.plusDays(7))); //next friday
    assertTrue(schedule.isPayday(nextPayday)); //payday
    PaymentPeriod resultPeriod = schedule.establishPaymentPeriod(nextPayday);//next period

    assertEquals(expected, resultPeriod);
    assertEquals(nextPayday.plusDays(1), schedule.getFirstDayOfNextPaymentPeriod());
  }

  @Test
  void shouldThrowWhenEndOfPeriodIsBeforeLastPayday(){
    String message = "Provided date is invalid";
    PaymentSchedule schedule = new BiweeklyPaymentSchedule(CREATED);
    LocalDate date = CREATED.minusDays(2);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> schedule.establishPaymentPeriod(date));

    assertEquals(message, exception.getMessage());
  }
}