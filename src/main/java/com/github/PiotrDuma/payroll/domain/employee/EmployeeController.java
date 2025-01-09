package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.tools.UUIDParser;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {
  private final ReceiveEmployee receiveEmployee;

  @Autowired
  public EmployeeController(ReceiveEmployee receiveEmployee) {
    this.receiveEmployee = receiveEmployee;
  }

  @GetMapping("/employees")
  public ResponseEntity<List<EmployeeDto>> getEmployees(){
    List<EmployeeDto> list = this.receiveEmployee.findAll().stream()
        .map(EmployeeResponse::toDto)
        .toList();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/employees/{id}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id")String id){
    UUID parsedId = UUIDParser.parse(id);
    EmployeeDto dto = this.receiveEmployee.find(new EmployeeId(parsedId)).toDto();
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }
}
