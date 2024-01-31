package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MockEmployeeRepository implements EmployeeRepository{
  private final Map<EmployeeId, Employee> repo;

  public MockEmployeeRepository() {
    this.repo = new HashMap<>();
  }
  @Override
  public Employee save(Employee employee) {
    EmployeeId id = employee.getId();
    return this.repo.put(id, employee);
  }

  @Override
  public List<Employee> findAll() {
    return repo.values().stream().toList();
  }
}
