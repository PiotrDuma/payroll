package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ReceiveEmployeeService implements ReceiveEmployee {
  private static final String EXCEPTION_MESSAGE = "Employee with id: %s not found";
  private final EmployeeRepository repository;

  @Autowired
  public ReceiveEmployeeService(EmployeeRepository repository) {
    this.repository = repository;
  }

  @Override
  public EmployeeResponse find(EmployeeId employeeId) {
    return repository.findById(employeeId)
        .orElseThrow(() -> new ResourceNotFoundException(String.format(EXCEPTION_MESSAGE, employeeId)));
  }

  @Override
  public List<EmployeeResponse> findAll() {
    return repository.findAll()
        .stream().map(e -> (EmployeeResponse) e)
        .collect(Collectors.toList());
  }
}
