package com.github.PiotrDuma.payroll.common.bank;

import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankConverter implements AttributeConverter<Bank, String> {
  private static final Logger log = LoggerFactory.getLogger(BankConverter.class);

  @Override
  public String convertToDatabaseColumn(Bank attribute) {
    log.info("Convert Bank value object to String");
    return attribute.getName();
  }

  @Override
  public Bank convertToEntityAttribute(String dbData) {
    log.info("Convert String to Bank value object");
    return new Bank(dbData);
  }
}
