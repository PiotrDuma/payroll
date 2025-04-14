package com.github.PiotrDuma.payroll.domain.employee.api.model;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.List;

public interface ReceiveEmployee {

  /**
   * Get employee by id, otherwise throw not found exception.
   * @param employeeId id
   * @return wrapped employee object
   * @throws ResourceNotFoundException when employee not found
   */
  EmployeeResponse find(EmployeeId employeeId);
  List<EmployeeResponse> findAll();
}
