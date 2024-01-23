package com.github.PiotrDuma.payroll.domain.employee.classification;

//TODO: validation requirements
public class CommissionRate {
  private double commissionRate;

  public CommissionRate(double commissionRate) {
    this.commissionRate = commissionRate;
  }

  public double getCommissionRate() {
    return commissionRate;
  }

  public void setCommissionRate(double commissionRate) {
    this.commissionRate = commissionRate;
  }
}
