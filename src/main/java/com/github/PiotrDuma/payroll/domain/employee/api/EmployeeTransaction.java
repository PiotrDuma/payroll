package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;

public interface EmployeeTransaction {
  EmployeeId execute();
}
