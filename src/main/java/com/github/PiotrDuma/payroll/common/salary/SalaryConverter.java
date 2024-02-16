package com.github.PiotrDuma.payroll.common.salary;

import jakarta.persistence.AttributeConverter;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalaryConverter implements AttributeConverter<Salary, BigDecimal> {
  private static final Logger log = LoggerFactory.getLogger(SalaryConverter.class);

  @Override
  public BigDecimal convertToDatabaseColumn(Salary attribute) {
    log.info("Convert Salary value object to BigDecimal");
    return attribute.getSalary();
  }

  @Override
  public Salary convertToEntityAttribute(BigDecimal dbData) {
    log.info("Convert BigDecimal to Salary value object");
    return new Salary(dbData);
  }
}
