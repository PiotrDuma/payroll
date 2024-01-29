package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.Amount;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
import com.github.PiotrDuma.payroll.domain.payment.schedule.PaymentPeriod;
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
    return null;
  }

  @Override
  public void addSalesReceipt(EmployeeId employeeId, LocalDate date, Amount amount) {
    this.salesReceipts.add(new SalesReceipt(employeeId, date, amount));
  }

  protected List<SalesReceipt> getSalesReceipts(){
    return this.salesReceipts;
  }
}
