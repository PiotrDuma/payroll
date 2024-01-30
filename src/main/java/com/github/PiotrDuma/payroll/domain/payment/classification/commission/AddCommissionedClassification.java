package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import org.springframework.stereotype.Service;

@Service
class AddCommissionedClassification implements CommissionedClassification {

  @Override
  public PaymentClassification getClassification(Salary salary, CommissionRate commissionRate) {
    return new CommissionedClassificationEntity(salary, commissionRate);
  }
}
