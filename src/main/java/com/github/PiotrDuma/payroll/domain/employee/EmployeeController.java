package com.github.PiotrDuma.payroll.domain.employee;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
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
  public ResponseEntity<EmployeeId> addEmployee(@RequestBody @Valid JsonNode dto,
      HttpServletRequest request) throws Exception{
    AddEmployeeTransaction addEmployeeTransaction = null;

    try{
      if(dto.has("commissionedRate")){
        addEmployeeTransaction = getAddCommissionedEmplyeeTransaction(dto);
      } else if (dto.has("hourlyRate")) {
        addEmployeeTransaction = getAddHourlyEmployeeTransaction(dto);
      } else if (dto.has("salary")) {
        addEmployeeTransaction = getAddSalariedEmployeeTransaction(dto);
      } else { //catch invalid json body request
        throw new InvalidArgumentException("Invalid Employee json body");      }
    }catch (Exception e) { //catch invalid json mapping
      throw new InvalidArgumentException("Invalid Employee json body");
    }

    EmployeeId id = addEmployeeTransaction.execute();

    return new ResponseEntity<>(id, HttpStatus.CREATED);
  }

  private AddEmployeeTransaction getAddSalariedEmployeeTransaction(JsonNode dto) {
    SalariedDto converted= new ObjectMapper().convertValue(dto, SalariedDto.class);
    return this.addEmployee.initSalariedEmployeeTransaction(
        new Address(converted.address()),
        new EmployeeName(converted.name()),
        new Salary(converted.salary()));
  }

  private AddEmployeeTransaction getAddHourlyEmployeeTransaction(JsonNode dto) {
    HourlyDto converted= new ObjectMapper().convertValue(dto, HourlyDto.class);
    return this.addEmployee.initHourlyEmployeeTransaction(
        new Address(converted.address()),
        new EmployeeName(converted.name()),
        new HourlyRate(converted.hourlyRate()));
  }

  private AddEmployeeTransaction getAddCommissionedEmplyeeTransaction(JsonNode dto) {
    CommissionedDto converted= new ObjectMapper().convertValue(dto, CommissionedDto.class);
    return this.addEmployee.initCommissionedEmployeeTransaction(
        new Address(converted.address()),
        new EmployeeName(converted.name()),
        new Salary(converted.salary()),
        new CommissionRate(converted.commissionedRate()));
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
  public record HourlyDto(@NotNull(message = "Address cannot empty") String address,
                            @NotNull(message = "Name cannot empty") String name,
                            @NotNull(message = "Hourly rate cannot be empty")
                            @DecimalMin(value = "0.0", message = "Hourly rate cannot be lower than 0") Double hourlyRate) {
    public HourlyDto(String address, String name, Double hourlyRate) {
      this.address = address;
      this.name = name;
      this.hourlyRate = hourlyRate;
    }
  }
  public record CommissionedDto(@NotNull(message = "Address cannot empty") String address,
                          @NotNull(message = "Name cannot empty") String name,
                          @NotNull(message = "Salary cannot be empty")
                          @DecimalMin(value = "0.0", message = "Salary cannot be lower than 0") Double salary,
                          @NotNull(message = "Commissioned rate cannot be empty")
                          @DecimalMin(value = "0.0", message = "Commissioned rate cannot be lower than 0") Double commissionedRate) {
    public CommissionedDto(String address, String name, Double salary, Double commissionedRate) {
      this.address = address;
      this.name = name;
      this.salary = salary;
      this.commissionedRate = commissionedRate;
    }
  }
}
