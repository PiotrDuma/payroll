package com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HoursTest {
  private Validator validator;

  @BeforeEach
  void setUp(){
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void shouldAllowSetDoubleValues(){
    Double value = 8.5;
    Hours hours = new Hours(value);

    assertEquals(value, hours.getHours());
  }

  @Test
  void shouldThrowWhenHoursObjectIsLowerThan0(){
    String message = "Number of hours cannot be lower than 0";
    Hours hours = new Hours(-1d);

    Set<ConstraintViolation<Hours>> violation = validator.validate(hours);

    assertEquals(1, violation.size());
    assertTrue(violation.stream().findFirst().isPresent());
    assertEquals(message, violation.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldThrowWhenHoursObjectIsGreaterThan24(){
    String message = "Number of hours cannot be greater than 24";
    Hours hours = new Hours(25d);

    Set<ConstraintViolation<Hours>> violation = validator.validate(hours);

    assertEquals(1, violation.size());
    assertTrue(violation.stream().findFirst().isPresent());
    assertEquals(message, violation.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldThrowWhenHoursIsNull(){
    String message = "Number of hours cannot be empty";
    Hours hours = new Hours(null);

    Set<ConstraintViolation<Hours>> violation = validator.validate(hours);

    assertEquals(1, violation.size());
    assertTrue(violation.stream().findFirst().isPresent());
    assertEquals(message, violation.stream().findFirst().get().getMessage());
  }
}