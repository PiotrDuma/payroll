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
  void shouldReturnOneDayPeriodWhenCreatedInPayDay(){
    LocalDate firstFriday = CREATED.plusDays(1); //PAYDAY: 1999-12-31, friday
    PaymentPeriod expected = new PaymentPeriod(firstFriday, firstFriday); // both 1999-12-31
    WeeklyPaymentSchedule schedule = new WeeklyPaymentSchedule(firstFriday);

    PaymentPeriod result = schedule.establishPaymentPeriod(firstFriday);

    assertEquals(expected, result);
    assertEquals(firstFriday.plusDays(1), schedule.getFirstDayOfNextPaymentPeriod());
  }

  @Test
  void shouldReturnTwoDaysPeriodWhenCreatedTheDayBeforePayDay(){
    LocalDate firstFriday = CREATED.plusDays(1); //PAYDAY: 1999-12-31, friday
    PaymentPeriod expected = new PaymentPeriod(CREATED, firstFriday); //1999-12-30 - 1999-12-31
    WeeklyPaymentSchedule schedule = new WeeklyPaymentSchedule(CREATED);

    PaymentPeriod result = schedule.establishPaymentPeriod(firstFriday);

    assertEquals(expected, result);
    assertEquals(firstFriday.plusDays(1), schedule.getFirstDayOfNextPaymentPeriod());
  }

  @Test
  void shouldReturnValidDatePeriodWeekAfterLastPayment(){
    LocalDate firstFriday = CREATED.plusDays(1); //PAYDAY: 1999-12-31, friday
    LocalDate secondFriday = CREATED.plusDays(8); //PAYDAY: 2000-01-07, friday
    PaymentPeriod expectedFirstPeriod = new PaymentPeriod(CREATED, firstFriday);//1999-12-30 - 1999-12-31
    PaymentPeriod expectedSecondPeriod = new PaymentPeriod(firstFriday.plusDays(1), secondFriday);//1999-01-01 - 2000-01-07
    WeeklyPaymentSchedule schedule = new WeeklyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(firstFriday));
    PaymentPeriod firstPeriod = schedule.establishPaymentPeriod(firstFriday);
    assertFalse(schedule.isPayday(secondFriday.minusDays(2))); //wednesday
    assertTrue(schedule.isPayday(secondFriday));
    PaymentPeriod secondPeriod = schedule.establishPaymentPeriod(secondFriday);

    assertEquals(expectedFirstPeriod, firstPeriod);
    assertEquals(expectedSecondPeriod, secondPeriod);
    assertEquals(secondFriday.plusDays(1), schedule.getFirstDayOfNextPaymentPeriod());
  }

  @Test
  void shouldReturnValidDatePeriodWhenLastWeekWasNotCounted(){ //Unlikely scenario
    LocalDate secondFriday = CREATED.plusDays(8); //PAYDAY: 2000-01-07, friday
    PaymentPeriod expected = new PaymentPeriod(CREATED, secondFriday); //1999-12-30 - 2000-01-07
    WeeklyPaymentSchedule schedule = new WeeklyPaymentSchedule(CREATED);

    assertTrue(schedule.isPayday(secondFriday));
    PaymentPeriod result = schedule.establishPaymentPeriod(secondFriday);

    assertEquals(expected, result);
    assertEquals(secondFriday.plusDays(1), schedule.getFirstDayOfNextPaymentPeriod());
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