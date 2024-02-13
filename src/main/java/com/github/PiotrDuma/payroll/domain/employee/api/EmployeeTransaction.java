package com.github.PiotrDuma.payroll.services.transaction;

import com.github.PiotrDuma.payroll.common.EmployeeId;

public interface EmployeeTransaction {
  EmployeeId execute();
}
