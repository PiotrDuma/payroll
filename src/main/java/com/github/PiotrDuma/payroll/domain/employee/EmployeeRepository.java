package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import java.util.List;
import java.util.Optional;

interface EmployeeRepository {
  Employee save(Employee employee);
  List<Employee> findAll();
  Optional<Employee> findById(EmployeeId employeeId);
}
