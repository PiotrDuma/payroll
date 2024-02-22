package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.common.salary.Salary;

public interface AddEmployeeTransactionFactory {
  AddEmployeeTransaction initSalariedEmployeeTransaction(Address address, EmployeeName name, Salary salary);
  AddEmployeeTransaction initHourlyEmployeeTransaction(Address address, EmployeeName name, HourlyRate hourlyRate);
  AddEmployeeTransaction initCommissionedEmployeeTransaction(Address address, EmployeeName name, Salary salary, CommissionRate commissionRate);
}
