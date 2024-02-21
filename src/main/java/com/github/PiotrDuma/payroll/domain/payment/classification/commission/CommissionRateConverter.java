package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CommissionRateConverter implements AttributeConverter<CommissionRate, Double> {
  private static final Logger log = LoggerFactory.getLogger(CommissionRateConverter.class);

  @Override
  public Double convertToDatabaseColumn(CommissionRate attribute) {
    log.info("Convert CommissionRate value object to double");
    return attribute.getCommissionRate();
  }

  @Override
  public CommissionRate convertToEntityAttribute(Double dbData) {
    log.info("Convert double to CommissionRate value object");
    return new CommissionRate(dbData);
  }
}
