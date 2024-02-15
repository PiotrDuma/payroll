package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CountMembershipChargesTransaction implements UnionTransaction {
  private static final Logger log = LoggerFactory.getLogger(CountMembershipChargesTransaction.class);

  private final UnionAffiliationRepository repository;
  private final EmployeeId employeeId;
  private final PaymentPeriod period;

  public CountMembershipChargesTransaction(UnionAffiliationRepository repository,
      EmployeeId employeeId, PaymentPeriod period) {
    this.repository = repository;
    this.employeeId = employeeId;
    this.period = period;
  }

  @Override
  public Object execute() {
    BigDecimal value = this.repository.findAll().stream()
        .filter(e -> e.isMember(employeeId))
        .flatMap(e -> e.getCharges().stream())
        .filter(e -> !e.getDate().isBefore(period.startPeriod()) && !e.getDate()
            .isAfter(period.endPeriod()))
        .map(UnionCharge::getAmount)
        .map(Amount::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return new Amount(value);
  }

  protected EmployeeId getEmployeeId() {
    return employeeId;
  }

  protected PaymentPeriod getPeriod() {
    return period;
  }
}
