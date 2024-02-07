package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.common.Amount;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

class CommissionedClassificationEntity implements PaymentClassification, SalesReceiptProvider{
  private final List<SalesReceipt> salesReceipts;
  private Salary salary;
  private CommissionRate commissionRate;

  public CommissionedClassificationEntity(Salary salary, CommissionRate commissionRate) {
    this.salesReceipts = new LinkedList<>();
    this.salary = salary;
    this.commissionRate = commissionRate;
  }

  @Override
  public Salary calculatePay(PaymentPeriod paymentPeriod) {
    BigDecimal sum = this.salesReceipts.stream()
        .filter(e -> e.getDate().isAfter(paymentPeriod.startPeriod()) &&
            e.getDate().isBefore(paymentPeriod.endPeriod()))
        .map(SalesReceipt::getAmount)
        .map(Amount::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .multiply(BigDecimal.valueOf(this.commissionRate.getCommissionRate() * 0.01))
        .add(this.salary.getSalary());
    return new Salary(sum);
  }

  @Override
  public void addSalesReceipt(EmployeeId employeeId, LocalDate date, Amount amount) {
    this.salesReceipts.add(new SalesReceipt(employeeId, date, amount));
  }

  protected List<SalesReceipt> getSalesReceipts(){
    return this.salesReceipts;
  }
}
