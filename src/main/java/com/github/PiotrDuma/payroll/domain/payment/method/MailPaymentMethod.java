package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.address.AddressConverter;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "mail_payment")
class MailPaymentMethod extends AbstractPaymentMethod implements PaymentMethod {
  private static final Logger log = LoggerFactory.getLogger(MailPaymentMethod.class);
  @Column(name = "address")
  @Convert(converter = AddressConverter.class)
  private Address address;

  protected MailPaymentMethod() {
  }

  protected MailPaymentMethod(Address address) {
    this.address = address;
  }

  @Override
  public void executePayment(LocalDate date, Salary salary) {
    log.info("Proceed mail payment with address: " + address.toString());
  }

  public Address getAddress() {
    return address;
  }
}
