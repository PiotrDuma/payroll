package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;

public interface ChangeEmployeeService {
  void changeNameTransaction(EmployeeId employeeId, EmployeeName name);
  void changeAddressTransaction(EmployeeId employeeId, Address address);

  void changeHourlyClassificationTransaction(EmployeeId employeeId, HourlyRate hourlyRate);
  void changeSalariedClassificationTransaction(EmployeeId employeeId, Salary salary);
  void changeCommissionedClassificationTransaction(EmployeeId employeeId, Salary salary, CommissionRate rate);

  void changeHoldPaymentMethodTransaction(EmployeeId employeeId, Bank bank, BankAccount account);
  void changeDirectPaymentMethodTransaction(EmployeeId employeeId);
  void changeHMailPaymentMethodTransaction(EmployeeId employeeId, Address address);
}
