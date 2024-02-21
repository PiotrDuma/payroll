package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.common.salary.SalaryConverter;
import com.github.PiotrDuma.payroll.domain.payment.classification.AbstractPaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "commissioned_classification")
class CommissionedClassificationEntity extends AbstractPaymentClassification
    implements PaymentClassification, SalesReceiptProvider{
  @Column(name = "salary")
  @Convert(converter = SalaryConverter.class)
  private Salary salary;
  @Column(name = "commission_rate")
  @Convert(converter = CommissionRateConverter.class)
  private CommissionRate commissionRate;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SalesReceipt> salesReceipts;

  protected CommissionedClassificationEntity() {
  }

  protected CommissionedClassificationEntity(Salary salary, CommissionRate commissionRate) {
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
