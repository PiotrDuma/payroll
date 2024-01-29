package com.github.PiotrDuma.payroll.domain.payment.schedule.api;

public interface PaymentScheduleFactory {
  PaymentSchedule getMonthlySchedule();
  PaymentSchedule getWeeklySchedule();
  PaymentSchedule getBiweeklySchedule();
}
