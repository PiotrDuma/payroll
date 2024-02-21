package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import jakarta.persistence.AttributeConverter;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class HourlyRateConverter implements AttributeConverter<HourlyRate, BigDecimal> {
  private static final Logger log = LoggerFactory.getLogger(HourlyRateConverter.class);

  @Override
  public BigDecimal convertToDatabaseColumn(HourlyRate attribute) {
    log.info("Convert HourlyRate value object to BigDecimal");
    return attribute.getHourlyRate();
  }

  @Override
  public HourlyRate convertToEntityAttribute(BigDecimal dbData) {
    log.info("Convert BigDecimal to HourlyRate value object");
    return new HourlyRate(dbData);
  }
}
