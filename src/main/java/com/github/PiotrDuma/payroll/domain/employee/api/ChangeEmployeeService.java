package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;

public interface ChangeEmployeeService {
  void changeNameTransaction(EmployeeId employeeId, EmployeeName name);
  void changeAddressTransaction(EmployeeId employeeId, Address address);

  /**
   * This method sets hourly classification salary calculation and weekly schedule payment.
   *
   * @param employeeId employee UUID value object
   * @param hourlyRate hourly revenue value object
   */
  void changeHourlyClassificationTransaction(EmployeeId employeeId, HourlyRate hourlyRate);

  /**
   * This method sets salaried classification calculation and monthly schedule payment.
   *
   * @param employeeId employee UUID value object
   * @param salary  monthly wage value object
   */
  void changeSalariedClassificationTransaction(EmployeeId employeeId, Salary salary);

  /**
   * This method sets commissioned classification calculation and biweekly schedule payment.
   *
   * @param employeeId employee UUID value object
   * @param salary  monthly wage value object
   * @param rate percent of commission rate bonus
   */
  void changeCommissionedClassificationTransaction(EmployeeId employeeId, Salary salary, CommissionRate rate);

  void changeHoldPaymentMethodTransaction(EmployeeId employeeId);
  void changeDirectPaymentMethodTransaction(EmployeeId employeeId, Bank bank, BankAccount account);
  void changeMailPaymentMethodTransaction(EmployeeId employeeId, Address address);
}
