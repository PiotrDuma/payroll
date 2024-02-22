package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.AbstractPaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "hourly_classification")
class HourlyClassificationEntity extends AbstractPaymentClassification
    implements PaymentClassification, TimeCardProvider {
  @Column(name = "hourly_rate")
  @Convert(converter = HourlyRateConverter.class)
  private HourlyRate hourlyRate;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private Set<TimeCard> timeCards;

  protected HourlyClassificationEntity() {
  }

  protected HourlyClassificationEntity(HourlyRate hourlyRate) {
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

  public void addOrUpdateTimeCard(EmployeeId employeeId, LocalDate date, Hours hours) {
    this.timeCards.stream()
        .filter(card -> card.getDate().equals(date))
        .findFirst()
        .ifPresentOrElse(card -> card.setHours(hours),
            () -> this.timeCards.add(new TimeCard(employeeId, date, hours)));
  }

  public HourlyRate getHourlyRate() {
    return hourlyRate;
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
