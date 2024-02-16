package com.github.PiotrDuma.payroll.common.BankAccount;

import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankAccountConverter implements AttributeConverter<BankAccount, String> {
  private static final Logger log = LoggerFactory.getLogger(BankAccount.class);

  @Override
  public String convertToDatabaseColumn(BankAccount attribute) {
    log.info("Convert BankAccount value object to String");
    return attribute.getNumber();
  }

  @Override
  public BankAccount convertToEntityAttribute(String dbData) {
    log.info("Convert String to BankAccount value object");
    return new BankAccount(dbData);
  }
}
