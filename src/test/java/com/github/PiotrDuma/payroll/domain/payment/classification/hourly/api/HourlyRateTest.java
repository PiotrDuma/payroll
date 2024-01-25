package com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HourlyRateTest {
  private Validator validator;

  @BeforeEach
  void setUp(){
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void shouldCreateValueObjectWithProvidedValue(){
    Double value = 25d;
    HourlyRate variable = new HourlyRate(value);

    assertEquals(value, variable.getHourlyRate().doubleValue());
  }

  @Test
  void shouldThrowWhenHourlyRateIsBelow0(){
    String message = "Hourly rate cannot be lower than 0";
    HourlyRate variable = new HourlyRate(-20);
    Set<ConstraintViolation<HourlyRate>> violations = validator.validate(variable);

    assertEquals(1, violations.size());
    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(message, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldReturnTrueWhenObjectsAreEquals(){
    HourlyRate variable1 = new HourlyRate(123d);
    HourlyRate variable2 = new HourlyRate(new BigDecimal(String.valueOf(123)));

    assertTrue(variable1.equals(variable2));
  }
}