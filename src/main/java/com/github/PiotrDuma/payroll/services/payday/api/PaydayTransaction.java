package com.github.PiotrDuma.payroll.services.payday.api;

public interface PaydayTransaction {
  /**
   * This service executes payments for all employees if it's their payday.
   * Salaries are calculated based on employee's contract and reduced by union affiliation dues.
   * Then paychecks are realised with employee's provided payment method and serialized to database.
   */
  void execute();
}
