package com.github.PiotrDuma.payroll.domain.payment.classification.commission.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AmountTest {
  private Validator validator;

  @BeforeEach
  void setUp(){
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void shouldThrowWhenAmountIsBelow0(){
    String message = "Amount cannot be lower than 0";
    Amount amount = new Amount(new BigDecimal(-2));
    Set<ConstraintViolation<Amount>> violations = validator.validate(amount);

    assertEquals(1, violations.size());
    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(message, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldThrowWhenAmountIsNull(){
    String message = "Amount cannot empty";
    BigDecimal value = null;
    Amount amount = new Amount(value);
    Set<ConstraintViolation<Amount>> violations = validator.validate(amount);

    assertEquals(1, violations.size());
    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(message, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldReturnTrueWhenObjectsAreEquals(){
    Amount amount1 = new Amount(123);
    Amount amount2 = new Amount(123);

    assertTrue(amount1.equals(amount2));
  }
}