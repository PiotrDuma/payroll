package com.github.PiotrDuma.payroll.domain.payment.schedule;

import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class PaymentScheduleFactoryService implements PaymentScheduleFactory {
  private final Clock clock;

  @Autowired
  public PaymentScheduleFactoryService(Clock clock) {
    this.clock = clock;
  }

  @Override
  public PaymentSchedule getMonthlySchedule() {
    return new MonthlyPaymentSchedule(LocalDate.now(this.clock));
  }

  @Override
  public PaymentSchedule getWeeklySchedule() {
    return new WeeklyPaymentSchedule(LocalDate.now(this.clock));
  }

  @Override
  public PaymentSchedule getBiweeklySchedule() {
    return new BiweeklyPaymentSchedule(LocalDate.now(this.clock));
  }
}
