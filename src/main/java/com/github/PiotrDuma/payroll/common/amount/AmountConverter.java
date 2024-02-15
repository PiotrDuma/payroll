package com.github.PiotrDuma.payroll.common.amount;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter(autoApply = true)
public class AmountConverter implements AttributeConverter<Amount, BigDecimal> {
  private static final Logger log = LoggerFactory.getLogger(AmountConverter.class);

  @Override
  public BigDecimal convertToDatabaseColumn(Amount attribute) {
    log.info("Convert Amount value object to BigDecimal");
    return attribute.getAmount();
  }

  @Override
  public Amount convertToEntityAttribute(BigDecimal dbData) {
    log.info("Convert BigDecimal to Amount value object");
    return new Amount(dbData);
  }
}
