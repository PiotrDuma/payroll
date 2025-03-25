package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeRequestDto.CommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeRequestDto.HourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeRequestDto.SalariedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import com.github.PiotrDuma.payroll.tools.UUIDParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
class EmployeeController {
  private final ReceiveEmployee receiveEmployee;
  private final AddEmployeeTransactionFactory addEmployee;

  @Autowired
  public EmployeeController(ReceiveEmployee receiveEmployee,
      AddEmployeeTransactionFactory addEmployee) {
    this.receiveEmployee = receiveEmployee;
    this.addEmployee = addEmployee;
  }

  @GetMapping
  public ResponseEntity<List<EmployeeDto>> getEmployees(){
    List<EmployeeDto> list = this.receiveEmployee.findAll().stream()
        .map(EmployeeResponse::toDto)
        .toList();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id")String id){
    UUID parsedId = UUIDParser.parse(id);
    EmployeeDto dto = this.receiveEmployee.find(new EmployeeId(parsedId)).toDto();
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeId> addEmployee(@RequestBody @Valid AddEmployeeDto dto,
      HttpServletRequest request) throws Exception{
    AddEmployeeTransaction addEmployeeTransaction = null;


    if(dto.commissionedDto() != null){
      addEmployeeTransaction = getAddCommissionedEmplyeeTransaction(dto);
    } else if (dto.hourlyDto() != null) {
      addEmployeeTransaction = getAddHourlyEmployeeTransaction(dto);
    } else if (dto.salariedDto() != null) {
      addEmployeeTransaction = getAddSalariedEmployeeTransaction(dto);
    } else { //catch invalid json body request
      throw new InvalidArgumentException("Invalid Employee json body");
    }

    EmployeeId id = addEmployeeTransaction.execute();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/employees/" + id);

    return new ResponseEntity<>(id, headers, HttpStatus.CREATED);
  }

  private AddEmployeeTransaction getAddSalariedEmployeeTransaction(AddEmployeeDto dto) {
    SalariedDto converted = dto.salariedDto();
    return this.addEmployee.initSalariedEmployeeTransaction(
        new Address(converted.address()),
        new EmployeeName(converted.name()),
        new Salary(converted.salary()));
  }

  private AddEmployeeTransaction getAddHourlyEmployeeTransaction(AddEmployeeDto dto) {
    HourlyDto converted = dto.hourlyDto();
    return this.addEmployee.initHourlyEmployeeTransaction(
        new Address(converted.address()),
        new EmployeeName(converted.name()),
        new HourlyRate(converted.hourlyRate()));
  }

  private AddEmployeeTransaction getAddCommissionedEmplyeeTransaction(AddEmployeeDto dto) {
    CommissionedDto converted = dto.commissionedDto();
    return this.addEmployee.initCommissionedEmployeeTransaction(
        new Address(converted.address()),
        new EmployeeName(converted.name()),
        new Salary(converted.salary()),
        new CommissionRate(converted.commissionedRate()));
  }

  public record AddEmployeeDto(@Valid SalariedDto salariedDto,
                               @Valid HourlyDto hourlyDto,
                               @Valid CommissionedDto commissionedDto){
    public AddEmployeeDto (SalariedDto salariedDto,
                        HourlyDto hourlyDto,
                        CommissionedDto commissionedDto){
      this.salariedDto = salariedDto;
      this.hourlyDto = hourlyDto;
      this.commissionedDto = commissionedDto;
    }
  }
}
