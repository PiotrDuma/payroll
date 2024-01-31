package com.github.PiotrDuma.payroll.common;

import jakarta.validation.constraints.NotNull;

public class Bank {
  private static final String NULL_VALUE = "Bank name cannot be null";
  @NotNull(message = NULL_VALUE)
  private final String name;

  public Bank(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return this.name.toString();
  }
}
