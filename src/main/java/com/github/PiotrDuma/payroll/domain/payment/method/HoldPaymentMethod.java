package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentDto;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class HoldPaymentMethod implements PaymentMethod {
  Logger log = LoggerFactory.getLogger(HoldPaymentMethod.class);

  private UUID id;
  private List<PaymentEntity> payments;

  public HoldPaymentMethod() {
    this.id = UUID.randomUUID();
    this.payments = new LinkedList<>();
  }

  @Override
  public void executePayment(LocalDate date, Salary salary) {
    PaymentEntity payment = new PaymentEntity(date, salary);
    log.info("Proceed hold payment method: salary provided to another department");
    this.payments.add(payment);
    log.info("Payment "+ payment.getId().toString() + " executed");
  }

  public List<PaymentDto> getPayments() {
    return payments.stream()
        .map(PaymentEntity::toPaymentDto)
        .collect(Collectors.toList());
  }
}
