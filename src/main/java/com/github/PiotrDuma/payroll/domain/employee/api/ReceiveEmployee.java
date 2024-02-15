package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import java.util.List;

public interface ReceiveEmployee {
  EmployeeResponse find(EmployeeId employeeId);
  List<EmployeeResponse> findAll();
}
