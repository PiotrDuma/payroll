package com.github.PiotrDuma.payroll.domain.payment.schedule.api;

import com.github.PiotrDuma.payroll.domain.payment.schedule.PaymentSchedule;

public interface PaymentScheduleFactory {
  PaymentSchedule getMonthlySchedule();
  PaymentSchedule getWeeklySchedule();
  PaymentSchedule getBiweeklySchedule();
}
