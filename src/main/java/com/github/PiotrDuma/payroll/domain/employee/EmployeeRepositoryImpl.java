package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

//TODO: remove this, implement infrastructure.
@Repository
class EmployeeRepositoryImpl implements EmployeeRepository{

  @Override
  public Employee save(Employee employee) {
    throw new RuntimeException("EmployeeRepository class not implemented");
  }

  @Override
  public List<Employee> findAll() {
    throw new RuntimeException("EmployeeRepository class not implemented");
  }

  @Override
  public Optional<Employee> findById(EmployeeId employeeId) {
    throw new RuntimeException("EmployeeRepository class not implemented");
  }
}
