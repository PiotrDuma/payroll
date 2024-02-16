package com.github.PiotrDuma.payroll.common.address;

import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressConverter implements AttributeConverter<Address, String> {
  private static final Logger log = LoggerFactory.getLogger(AddressConverter.class);

  @Override
  public String convertToDatabaseColumn(Address attribute) {
    log.info("Convert Address value object to String");
    return attribute.getAddress();
  }

  @Override
  public Address convertToEntityAttribute(String dbData) {
    log.info("Convert String to Address value object");
    return new Address(dbData);
  }
}
