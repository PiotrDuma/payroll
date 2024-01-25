package com.github.PiotrDuma.payroll.domain.payment.classification.salary;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.salary.api.SalariedClassification;
import com.github.PiotrDuma.payroll.domain.payment.schedule.PaymentPeriod;
import org.springframework.stereotype.Service;

@Service
class AddSalariedClassification implements SalariedClassification, PaymentClassification {
  private static final String EXCEPTION = "Classification must contain salary object";
  private SalariedClassificationEntity entity = null;

  public AddSalariedClassification() {
  }

  @Override
  public Salary calculatePay(PaymentPeriod paymentPeriod) {
    return entity.calculatePay(paymentPeriod);
  }

  @Override
  public PaymentClassification getClassification(Salary salary) {
    if(salary == null){
      throw new IllegalArgumentException(EXCEPTION);
    }
    this.entity = new SalariedClassificationEntity(salary);
    return entity;
  }
}
