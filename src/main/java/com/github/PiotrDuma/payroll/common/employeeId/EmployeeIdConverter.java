package com.github.PiotrDuma.payroll.common.employeeId;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Converter(autoApply = true)
public class EmployeeIdConverter implements AttributeConverter<EmployeeId, UUID> {
  private static final Logger log = LoggerFactory.getLogger(EmployeeIdConverter.class);

  @Override
  public UUID convertToDatabaseColumn(EmployeeId attribute) {
    log.info("Convert EmployeeId value object to UUID");
    return attribute.getId();
  }

  @Override
  public EmployeeId convertToEntityAttribute(UUID dbData) {
    log.info("Convert UUID to EmployeeId value object");
    return new EmployeeId(dbData);
  }
}
