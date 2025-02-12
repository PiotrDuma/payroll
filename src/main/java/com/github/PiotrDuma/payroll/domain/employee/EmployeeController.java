package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.tools.UUIDParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeController {
  private final ReceiveEmployee receiveEmployee;
  private final AddEmployeeTransactionFactory addEmployee;

  @Autowired
  public EmployeeController(ReceiveEmployee receiveEmployee,
      AddEmployeeTransactionFactory addEmployee) {
    this.receiveEmployee = receiveEmployee;
    this.addEmployee = addEmployee;
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

  @PostMapping(value = "/employees", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeId> addEmployee(@RequestBody @Valid SalariedDto dto,
      HttpServletRequest request){
    AddEmployeeTransaction addEmployeeTransaction = this.addEmployee.initSalariedEmployeeTransaction(
        new Address(dto.address()),
        new EmployeeName(dto.name()),
        new Salary(dto.salary()));

    EmployeeId id = addEmployeeTransaction.execute();
    return new ResponseEntity<>(id, HttpStatus.CREATED);
  }

  public record SalariedDto(@NotNull(message = "Address cannot empty") String address,
                            @NotNull(message = "Name cannot empty") String name,
                            @NotNull(message = "Salary cannot be empty")
                            @DecimalMin(value = "0.0", message = "Salary cannot be lower than 0") Double salary){

    public SalariedDto(String address, String name,  Double salary) {
      this.address = address;
      this.name = name;
      this.salary = salary;
    }
  }
}
