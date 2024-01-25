package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import com.github.PiotrDuma.payroll.domain.payment.schedule.PaymentPeriod;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class HourlyClassificationEntity implements PaymentClassification, TimeCardProvider {
  private HourlyRate hourlyRate;
  private Set<TimeCard> timeCards;

  public HourlyClassificationEntity(HourlyRate hourlyRate) {
    this.hourlyRate = hourlyRate;
    this.timeCards = new HashSet<>();
  }

  @Override
  public Salary calculatePay(PaymentPeriod paymentPeriod) {
    List<BigDecimal> dailyWages = this.timeCards.stream()
        .filter(e -> e.getDate().isAfter(paymentPeriod.startPeriod()) && e.getDate()
            .isBefore(paymentPeriod.endPeriod()))
        .map(this::countDailyWage)
        .toList();
    return new Salary(dailyWages.stream()
        .reduce(BigDecimal.ZERO, BigDecimal::add));
  }

  public void addOrUpdateTimeCard(LocalDate date, Hours hours) {
    this.timeCards.stream()
        .filter(card -> card.getDate().equals(date))
        .findFirst()
        .ifPresentOrElse(card -> card.setHours(hours),
            () -> this.timeCards.add(new TimeCard(date, hours)));
  }

  public Set<TimeCard> getTimeCards() {
    return timeCards;
  }

  private BigDecimal countDailyWage(TimeCard timeCard){
    Double hours = timeCard.getHours().getHours();
    BigDecimal wage;

    if(hours <= 8){
      wage = this.hourlyRate.getHourlyRate().multiply(new BigDecimal(String.valueOf(hours)));
    }else{
      BigDecimal standardTime = new BigDecimal(String.valueOf(8));
      BigDecimal extraTime = new BigDecimal(String.valueOf(hours - 8));
      wage = this.hourlyRate.getHourlyRate().multiply(standardTime);
      wage = wage.add(this.hourlyRate.getHourlyRate()
          .multiply(extraTime)
          .multiply(new BigDecimal(String.valueOf(1.5))));
    }
    return wage;
  }
}
