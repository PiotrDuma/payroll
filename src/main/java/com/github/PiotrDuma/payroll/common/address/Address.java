package com.github.PiotrDuma.payroll.common.address;

import jakarta.validation.constraints.NotNull;

public class Address {
  @NotNull(message = "Address cannot empty")
  private String address;

  public Address() {
  }

  public Address(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return address;
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof Address o)){
      return false;
    }
    return o.getAddress().equals(address);
  }
}
