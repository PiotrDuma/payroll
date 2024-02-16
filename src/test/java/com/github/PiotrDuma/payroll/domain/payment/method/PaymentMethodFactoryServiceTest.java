package com.github.PiotrDuma.payroll.domain.payment.method;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentMethodFactoryServiceTest {
  private PaymentMethodFactory factory;

  @BeforeEach
  void setUp(){
    this.factory = new PaymentMethodFactoryService();
  }

  @Test
  void shouldReturnHoldMethodClassObject(){
    PaymentMethod paymentMethod = this.factory.getHoldPaymentMethod();

    assertTrue(paymentMethod instanceof HoldPaymentMethod);
  }

  @Test
  void shouldReturnMailMethodClassObject(){
    PaymentMethod paymentMethod = this.factory.getMailPaymentMethod(new Address("address"));

    assertTrue(paymentMethod instanceof MailPaymentMethod);
  }

  @Test
  void shouldReturnDirectMethodClassObject(){
    PaymentMethod paymentMethod = this.factory.getDirectPaymentMethod(new Bank("BANK"),
        new BankAccount("01234567890123456789012345"));

    assertTrue(paymentMethod instanceof DirectPaymentMethod);
  }
}