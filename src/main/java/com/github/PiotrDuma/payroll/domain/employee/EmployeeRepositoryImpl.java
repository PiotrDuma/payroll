package com.github.PiotrDuma.payroll.domain.employee;

import java.util.List;
import org.springframework.stereotype.Repository;

//TODO: remove this, implement infrastructure.
@Repository
class EmployeeRepositoryImpl implements EmployeeRepository{

  @Override
  public Employee save(Employee employee) {
    return null;
  }

  @Override
  public List<Employee> findAll() {
    return null;
  }
}
