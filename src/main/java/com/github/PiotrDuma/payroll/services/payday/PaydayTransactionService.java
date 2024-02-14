package com.github.PiotrDuma.payroll.services.payday;

import com.github.PiotrDuma.payroll.common.Amount;
import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
import com.github.PiotrDuma.payroll.services.payday.api.PaydayTransaction;
import java.time.Clock;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class PaydayTransactionService implements PaydayTransaction {
  private static final Logger log = LoggerFactory.getLogger(PaydayTransactionService.class);
  private static final String MESSAGE = "%s: PAYDAY EXECUTED";
  private final ReceiveEmployee receiveEmployee;
  private final UnionAffiliationService unionService;
  private final Clock clock;
  private final PaycheckRepository paycheckRepository;
  private LocalDate today;

  @Autowired
  public PaydayTransactionService(ReceiveEmployee receiveEmployee,
      UnionAffiliationService unionServoce, Clock clock, PaycheckRepository paycheckRepository) {
    this.receiveEmployee = receiveEmployee;
    this.unionService = unionServoce;
    this.clock = clock;
    this.paycheckRepository = paycheckRepository;
    this.today = LocalDate.now(clock);
  }

  @Override
  public void execute() {
    this.receiveEmployee.findAll().stream()
        .filter(e -> e.getSchedule().isPayday(today))
        .forEach(e-> {
          PaymentPeriod paymentPeriod = e.getSchedule().establishPaymentPeriod(today);
          Salary salary = e.getPaymentClassification().calculatePay(paymentPeriod);
          Amount unionDues = unionService.countMembershipCharges(e.getId(), paymentPeriod);
          PaycheckEntity paycheck = new PaycheckEntity(e.getId(), salary, unionDues, today);
          e.getPaymentMethod().executePayment(today, paycheck.getNetSalary());
          this.paycheckRepository.save(paycheck);
        });
    log.info(String.format(MESSAGE, today.toString()));
  }
}
