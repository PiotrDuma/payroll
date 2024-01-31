package com.github.PiotrDuma.payroll.domain.employee;

import java.util.List;

interface EmployeeRepository {
  Employee save(Employee employee);
  List<Employee> findAll();
}
