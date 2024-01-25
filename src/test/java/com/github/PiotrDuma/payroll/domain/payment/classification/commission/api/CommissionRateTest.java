package com.github.PiotrDuma.payroll.domain.payment.classification.commission.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommissionRateTest {

  private Validator validator;

  @BeforeEach
  void setUp(){
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void shouldCreateValueObjectWithProvidedValue(){
    Double value = 25d;
    CommissionRate variable = new CommissionRate(value);

    assertEquals(value, variable.getCommissionRate());
  }

  @Test
  void shouldThrowWhenCommissionRateIsBelow0(){
    String message = "Commission rate cannot be lower than 0%";
    CommissionRate variable = new CommissionRate(-20);
    Set<ConstraintViolation<CommissionRate>> violations = validator.validate(variable);

    assertEquals(1, violations.size());
    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(message, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldThrowWhenCommissionRateIsOver100(){
    String message = "Commission rate cannot be greater than 100%";
    CommissionRate variable = new CommissionRate(110);
    Set<ConstraintViolation<CommissionRate>> violations = validator.validate(variable);

    assertEquals(1, violations.size());
    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(message, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldReturnTrueWhenObjectsAreEquals(){
    CommissionRate variable1 = new CommissionRate(123);
    CommissionRate variable2 = new CommissionRate(123);

    assertTrue(variable1.equals(variable2));
  }

}