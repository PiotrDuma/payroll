package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EmployeeNameConverter implements AttributeConverter<EmployeeName, String> {
  private static final Logger log = LoggerFactory.getLogger(EmployeeNameConverter.class);

  @Override
  public String convertToDatabaseColumn(EmployeeName attribute) {
    log.info("Convert EmployeeName value object to String");
    return attribute.getName();
  }

  @Override
  public EmployeeName convertToEntityAttribute(String dbData) {
    log.info("Convert String to EmployeeName value object");
    return new EmployeeName(dbData);
  }
}
