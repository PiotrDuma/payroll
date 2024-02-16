package com.github.PiotrDuma.payroll.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.salary.Salary;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalaryTest {
  private Validator validator;

  @BeforeEach
  void setUp(){
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void shouldThrowWhenSalaryIsBelow0(){
    String message = "Salary cannot be lower than 0";
    Salary salary = new Salary(new BigDecimal(-2));
    Set<ConstraintViolation<Salary>> violations = validator.validate(salary);

    assertEquals(1, violations.size());
    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(message, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldThrowWhenSalaryIsNull(){
    String message = "Salary cannot empty";
    BigDecimal value = null;
    Salary salary = new Salary(value);
    Set<ConstraintViolation<Salary>> violations = validator.validate(salary);

    assertEquals(1, violations.size());
    assertTrue(violations.stream().findFirst().isPresent());
    assertEquals(message, violations.stream().findFirst().get().getMessage());
  }

  @Test
  void shouldReturnTrueWhenObjectsAreEquals(){
    Salary salary1 = new Salary(123);
    Salary salary2 = new Salary(123);

    assertTrue(salary1.equals(salary2));
  }
}