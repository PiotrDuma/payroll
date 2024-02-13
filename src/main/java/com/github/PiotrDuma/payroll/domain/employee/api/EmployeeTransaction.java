package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.common.EmployeeId;

public interface EmployeeTransaction {
  EmployeeId execute();
}
