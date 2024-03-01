package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class AddHourlyClassification implements HourlyClassification {

  @Override
  public PaymentClassification getClassification(HourlyRate hourlyRate) {
    return new HourlyClassificationEntity(hourlyRate);
  }
}
