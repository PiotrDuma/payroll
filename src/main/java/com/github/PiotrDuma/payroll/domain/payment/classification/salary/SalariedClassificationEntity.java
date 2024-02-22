package com.github.PiotrDuma.payroll.domain.payment.classification.salary;

import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.common.salary.SalaryConverter;
import com.github.PiotrDuma.payroll.domain.payment.classification.AbstractPaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 * Flat salary value, refactor if it should be calculated based on the days of payment period.
 */
@Entity
@Table(name = "salaried_classification")
class SalariedClassificationEntity extends AbstractPaymentClassification implements PaymentClassification {
  @Column(name = "salary")
  @Convert(converter = SalaryConverter.class)
  private Salary salary;

  protected SalariedClassificationEntity() {
  }

  public SalariedClassificationEntity(Salary salary) {
    this.salary = salary;
  }

  @Override
  public Salary calculatePay(PaymentPeriod paymentPeriod) {
    return salary;
  }

  public Salary getSalary() {
    return salary;
  }
}
