package com.github.PiotrDuma.payroll.common;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * Primitive simplified account value object.
 */
public class BankAccount {
  private static final String NULL_VALUE = "Account number cannot be null";
  private static final String INVALID_FORMAT = "Account number format is invalid";
  private static final String NUMBERS_ONLY = "Account number must contain only number characters";

  @NotNull(message = NULL_VALUE)
  @Length(min = 26, max = 26, message = INVALID_FORMAT)
  @Pattern(regexp = "\\d+", message = NUMBERS_ONLY)
  private final String number;

  public BankAccount(String number) {
    this.number = number;
  }

  public String getNumber() {
    return number;
  }

  @Override
  public String toString() {
    return this.number.toString();
  }
}
