package com.github.PiotrDuma.payroll.domain.payment.classification.salary;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.salary.api.SalariedClassification;
import org.springframework.stereotype.Service;

@Service
class AddSalariedClassification implements SalariedClassification {
  private static final String EXCEPTION = "Classification must contain salary object";

  @Override
  public PaymentClassification getClassification(Salary salary) {
    if(salary == null){
      throw new IllegalArgumentException(EXCEPTION);
    }
    return new SalariedClassificationEntity(salary);
  }
}
