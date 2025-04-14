package com.github.PiotrDuma.payroll.common.bank;

import jakarta.validation.constraints.NotNull;

public class Bank {
  private static final String NULL_VALUE = "Bank name cannot be null";
  @NotNull(message = NULL_VALUE)
  private String name;

  public Bank() {
  }

  public Bank(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof Bank o)){
      return false;
    }
    return o.getName().equals(this.name);
  }
}
