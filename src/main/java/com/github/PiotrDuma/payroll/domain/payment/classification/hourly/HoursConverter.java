package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class HoursConverter implements AttributeConverter<Hours, Double> {
  private static final Logger log = LoggerFactory.getLogger(HoursConverter.class);

  @Override
  public Double convertToDatabaseColumn(Hours attribute) {
    log.info("Convert Hours value object to double");
    return attribute.getHours();
  }

  @Override
  public Hours convertToEntityAttribute(Double dbData) {
    log.info("Convert double to Hours value object");
    return new Hours(dbData);
  }
}
