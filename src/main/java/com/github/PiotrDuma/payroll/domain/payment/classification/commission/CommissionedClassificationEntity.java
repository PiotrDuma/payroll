package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.Amount;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
import com.github.PiotrDuma.payroll.domain.payment.schedule.PaymentPeriod;
import java.time.LocalDate;

class CommissionedClassificationEntity implements PaymentClassification, SalesReceiptProvider{

  @Override
  public Salary calculatePay(PaymentPeriod paymentPeriod) {
    return null;
  }

  @Override
  public void addSalesReceipt(EmployeeId employeeId, LocalDate date, Amount amount) {

  }
}
