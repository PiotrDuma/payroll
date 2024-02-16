package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.address.Address;
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

class MailPaymentMethod implements PaymentMethod {
  Logger log = LoggerFactory.getLogger(MailPaymentMethod.class);

  private UUID id;
  private Address address;
  private List<PaymentEntity> payments;

  public MailPaymentMethod(Address address) {
    this.id = UUID.randomUUID();
    this.address = address;
    this.payments = new LinkedList<>();
  }

  @Override
  public void executePayment(LocalDate date, Salary salary) {
    PaymentEntity payment = new PaymentEntity(date, salary);
    log.info("Proceed mail payment with address: " + address.toString());
    this.payments.add(payment);
    log.info("Payment "+ payment.getId().toString() + " executed");
  }

  public List<PaymentDto> getPayments() {
    return payments.stream()
        .map(PaymentEntity::toPaymentDto)
        .collect(Collectors.toList());
  }

  public Address getAddress() {
    return address;
  }
}
