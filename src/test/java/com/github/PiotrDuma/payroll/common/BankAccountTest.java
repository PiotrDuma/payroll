package com.github.PiotrDuma.payroll.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.BankAccount.BankAccount;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankAccountTest {

  private Validator validator;

  @BeforeEach
  void setUp(){
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void shouldThrowWhenValueIsNull(){
    String expectedMessage = "Account number cannot be null";
    BankAccount valueObject = new BankAccount(null);

    Set<ConstraintViolation<BankAccount>> violations = this.validator.validate(valueObject);

    assertEquals(1, violations.size());

    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(expectedMessage, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldThrowWhenValueContainsNotNumberCharacter(){
    String expectedMessage = "Account number must contain only number characters";
    String number = "A1234567890123456789012345";
    BankAccount valueObject = new BankAccount(number);

    Set<ConstraintViolation<BankAccount>> violations = this.validator.validate(valueObject);

    assertEquals(1, violations.size());

    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(expectedMessage, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldThrowWhenValueLengthIsInvalid(){
    String expectedMessage = "Account number format is invalid";
    String number = "0123456789";
    BankAccount valueObject = new BankAccount(number);

    Set<ConstraintViolation<BankAccount>> violations = this.validator.validate(valueObject);

    assertEquals(1, violations.size());

    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(expectedMessage, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldCreateObjectWithValidValue(){
    String number = "01234567890123456789012345";
    BankAccount valueObject = new BankAccount(number);

    Set<ConstraintViolation<BankAccount>> violations = this.validator.validate(valueObject);

    assertEquals(0, violations.size());
  }
}