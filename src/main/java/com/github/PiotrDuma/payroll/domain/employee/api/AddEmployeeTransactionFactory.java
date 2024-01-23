package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.domain.employee.classification.CommissionRate;
import com.github.PiotrDuma.payroll.domain.employee.classification.HourlyRate;
import com.github.PiotrDuma.payroll.domain.employee.classification.Salary;

public interface AddEmployeeTransactionFactory {
  AddEmployeeTransaction initSalariedEmployeeTransaction(Address address, EmployeeName name, Salary salary);
  AddEmployeeTransaction initHourlyEmployeeTransaction(Address address, EmployeeName name, HourlyRate hourlyRate);
  AddEmployeeTransaction initSalariedEmployeeTransaction(Address address, EmployeeName name, Salary salary, CommissionRate commissionRate);
}
