package com.github.PiotrDuma.payroll.common.address;

public class Address {
  private String address;

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
}
