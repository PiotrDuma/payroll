package com.github.PiotrDuma.payroll.domain.payment.schedule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentScheduleFactoryServiceTest {
  private static ZonedDateTime NOW = ZonedDateTime.of(
      2000,
      1,
      1,
      20,
      0,
      0,
      0,
      ZoneId.of("UTC"));

  @Mock
  private Clock clock;
  private PaymentScheduleFactory factory;

  @BeforeEach
  void setUp(){
    this.factory = new PaymentScheduleFactoryService(clock);
    when(this.clock.instant()).thenReturn(NOW.toInstant());
    when(this.clock.getZone()).thenReturn(NOW.getZone());

  }

  @Test
  void shouldReturnMonthlyPaymentScheduleObjectWithGivenCreatedDate(){
    PaymentSchedule object = this.factory.getMonthlySchedule();

    assertTrue(object instanceof MonthlyPaymentSchedule);

    LocalDate date = ((MonthlyPaymentSchedule) object).getFirstDayOfNextPaymentPeriod();
    assertEquals(LocalDate.now(this.clock), date);
  }

  @Test
  void shouldReturnWeeklyPaymentScheduleObjectWithGivenCreatedDate(){
    PaymentSchedule object = this.factory.getWeeklySchedule();

    assertTrue(object instanceof WeeklyPaymentSchedule);

    LocalDate date = ((WeeklyPaymentSchedule) object).getFirstDayOfNextPaymentPeriod();
    assertEquals(LocalDate.now(this.clock), date);
  }

  @Test
  void shouldReturnBiweeklyPaymentScheduleObjectWithGivenCreatedDate(){
    PaymentSchedule object = this.factory.getBiweeklySchedule();

    assertTrue(object instanceof BiweeklyPaymentSchedule);

    LocalDate date = ((BiweeklyPaymentSchedule) object).getFirstDayOfNextPaymentPeriod();
    assertEquals(LocalDate.now(this.clock), date);
  }
}