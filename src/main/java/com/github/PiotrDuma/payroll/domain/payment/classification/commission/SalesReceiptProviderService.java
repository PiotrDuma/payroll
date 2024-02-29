package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SalesReceiptProviderService implements SalesReceiptProvider {
  private static final Logger log = LoggerFactory.getLogger(SalesReceiptProviderService.class);
  private static final String MESSAGE = "Employee %s cannot record sales receipts";
  private final ReceiveEmployee employeeRepo;

  @Autowired
  public SalesReceiptProviderService(ReceiveEmployee employeeRepo) {
    this.employeeRepo = employeeRepo;
  }

  @Override
  public void addSalesReceipt(EmployeeId employeeId, LocalDate date, Amount amount) {
    CommissionedClassificationEntity classification = getCommissionedClassificationEntity(employeeId);
    classification.addSalesReceipt(employeeId, date, amount);
  }

  private CommissionedClassificationEntity getCommissionedClassificationEntity(EmployeeId employeeId){
    PaymentClassification paymentClassification = this.employeeRepo.find(employeeId)
        .getPaymentClassification();
    try{
      return (CommissionedClassificationEntity) paymentClassification;
    }catch (Exception e){
      log.info("PaymentClassification casting failed: " + this.getClass().getName());
      throw new InvalidArgumentException(String.format(MESSAGE, employeeId));
    }
  }
}
