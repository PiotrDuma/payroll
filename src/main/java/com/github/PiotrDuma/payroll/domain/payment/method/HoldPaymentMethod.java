package com.github.PiotrDuma.payroll.domain.payment.method;

import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "hold_payment")
class HoldPaymentMethod extends AbstractPaymentMethod implements PaymentMethod {
  private static final Logger log = LoggerFactory.getLogger(HoldPaymentMethod.class);

  protected HoldPaymentMethod() {
  }

  @Override
  public void executePayment(LocalDate date, Salary salary) {
    log.info("Proceed hold payment method: salary provided to another department");
  }


}
