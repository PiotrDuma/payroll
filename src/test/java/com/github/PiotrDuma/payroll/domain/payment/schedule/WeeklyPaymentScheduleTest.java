package com.github.PiotrDuma.payroll.domain.payment.schedule;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentPeriod;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class WeeklyPaymentScheduleTest {
  private static final LocalDate CREATED = LocalDate.of(1999, 12,30);

  @Test
  void shouldReturnFalseIfItsNotFriday(){
    PaymentSchedule schedule = new WeeklyPaymentSchedule(CREATED); //thursday

    assertFalse(schedule.isPayday(CREATED));
  }

  @Test
  void shouldReturnTrueIfItsFriday(){
    PaymentSchedule schedule = new WeeklyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(CREATED.plusDays(1)));
  }

  @Test
  void shouldReturnValidDatePeriod(){
    LocalDate endOfPeriod = CREATED.plusDays(1); //PAYDAY: 1999-12-31, friday
    PaymentPeriod expected = new PaymentPeriod(CREATED.plusDays(1), endOfPeriod); // both 1999-12-31
    WeeklyPaymentSchedule schedule = new WeeklyPaymentSchedule(CREATED);

    PaymentPeriod result = schedule.establishPaymentPeriod(endOfPeriod);

    assertEquals(expected, result);
    assertEquals(endOfPeriod, schedule.getLastPayDate());
  }

  @Test
  void shouldReturnValidDatePeriodWeekAfterLastPayment(){
    LocalDate endOfPeriod = CREATED.plusDays(1); //PAYDAY: 1999-12-31, friday
    LocalDate nextWeek = CREATED.plusDays(8); //PAYDAY: 2000-01-07, friday
    PaymentPeriod expectedFirstPeriod = new PaymentPeriod(CREATED.plusDays(1), endOfPeriod); // both 1999-12-31
    PaymentPeriod expectedSecondPeriod = new PaymentPeriod(endOfPeriod.plusDays(1), nextWeek); //1999-01-01 - 2000-01-07
    WeeklyPaymentSchedule schedule = new WeeklyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(endOfPeriod));
    PaymentPeriod result1 = schedule.establishPaymentPeriod(endOfPeriod);
    assertFalse(schedule.isPayday(nextWeek.minusDays(2))); //wednesday
    assertTrue(schedule.isPayday(nextWeek));
    PaymentPeriod result2 = schedule.establishPaymentPeriod(nextWeek);

    assertEquals(expectedFirstPeriod, result1);
    assertEquals(expectedSecondPeriod, result2);
    assertEquals(nextWeek, schedule.getLastPayDate());
  }

  @Test
  void shouldReturnValidDatePeriodWhenLastWeekWasNotCounted(){ //Unlikely scenario
    LocalDate endOfPeriod = CREATED.plusDays(8); //PAYDAY: 1999-12-31, friday
    PaymentPeriod expected = new PaymentPeriod(CREATED.plusDays(1), endOfPeriod); // both 1999-12-31
    WeeklyPaymentSchedule schedule = new WeeklyPaymentSchedule(CREATED);

    PaymentPeriod result = schedule.establishPaymentPeriod(endOfPeriod);

    assertEquals(expected, result);
    assertEquals(endOfPeriod, schedule.getLastPayDate());
  }

  @Test
  void shouldThrowWhenEndOfPeriodIsBeforeLastPayday(){
    String message = "Provided date is invalid";
    PaymentSchedule schedule = new WeeklyPaymentSchedule(CREATED);
    LocalDate date = CREATED.minusDays(2);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> schedule.establishPaymentPeriod(date));

    assertEquals(message, exception.getMessage());
  }
}