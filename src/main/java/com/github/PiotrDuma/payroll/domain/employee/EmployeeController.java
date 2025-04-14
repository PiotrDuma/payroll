package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddCommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddHourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddSalariedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.ReceiveEmployee;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import com.github.PiotrDuma.payroll.tools.UUIDParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final Logger log = LoggerFactory.getLogger(EmployeeController.class);
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
    log.debug("Request GET performed on '/employees' endpoint");
    List<EmployeeDto> list = this.receiveEmployee.findAll().stream()
        .map(EmployeeResponse::toDto)
        .toList();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") String id){
    log.debug(String.format("Request GET performed on '/employees/%s' endpoint", id));
    UUID parsedId = UUIDParser.parse(id);
    EmployeeDto dto = this.receiveEmployee.find(new EmployeeId(parsedId)).toDto();
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeId> addEmployee(@RequestBody @Valid AddEmployeeDto dto,
      HttpServletRequest request) throws Exception{
    log.debug("Request POST performed on '/employees' endpoint");
    AddEmployeeTransaction addEmployeeTransaction = null;


    if(dto.commissionedDto() != null){
      addEmployeeTransaction = getAddCommissionedEmployeeTransaction(dto.commissionedDto());
    } else if (dto.hourlyDto() != null) {
      addEmployeeTransaction = getAddHourlyEmployeeTransaction(dto.hourlyDto());
    } else if (dto.salariedDto() != null) {
      addEmployeeTransaction = getAddSalariedEmployeeTransaction(dto.salariedDto());
    } else { //catch invalid json body request
      throw new InvalidArgumentException("Invalid Employee json body");
    }

    EmployeeId id = addEmployeeTransaction.execute();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/employees/" + id);

    return new ResponseEntity<>(id, headers, HttpStatus.CREATED);
  }

//  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<EmployeeId> editEmployee(@RequestBody @Valid ChangeEmployeeDto dto,
//      HttpServletRequest request) throws Exception{
//    log.debug("Request PUT performed on '/employees' endpoint");
//    AddEmployeeTransaction addEmployeeTransaction = null;
//
//    if(dto.commissionedDto() != null){
//      addEmployeeTransaction = getAddCommissionedEmployeeTransaction(dto.commissionedDto());
//    } else if (dto.hourlyDto() != null) {
//      addEmployeeTransaction = getAddHourlyEmployeeTransaction(dto.hourlyDto());
//    } else if (dto.salariedDto() != null) {
//      addEmployeeTransaction = getAddSalariedEmployeeTransaction(dto.salariedDto());
//    } else { //catch invalid json body request
//      throw new InvalidArgumentException("Invalid Employee json body");
//    }
//
//    EmployeeId id = addEmployeeTransaction.execute();
//    HttpHeaders headers = new HttpHeaders();
//    headers.add("Location", "/employees/" + id);
//
//    return new ResponseEntity<>(id, headers, HttpStatus.CREATED);
//  }

  private AddEmployeeTransaction getAddCommissionedEmployeeTransaction(AddCommissionedDto dto) {
    return this.addEmployee.initCommissionedEmployeeTransaction(
        dto.address(), dto.name(), dto.classification().salary(), dto.classification().commissionedRate());
  }

  private AddEmployeeTransaction getAddHourlyEmployeeTransaction(AddHourlyDto dto) {
    return this.addEmployee.initHourlyEmployeeTransaction(
        dto.address(), dto.name(), dto.classification().hourlyRate());
  }

  private AddEmployeeTransaction getAddSalariedEmployeeTransaction(AddSalariedDto dto){
    return this.addEmployee.initSalariedEmployeeTransaction(
        dto.address(), dto.name(), dto.classification().salary());
  }

  public record AddEmployeeDto(@Valid AddSalariedDto salariedDto,
                               @Valid AddHourlyDto hourlyDto,
                               @Valid AddCommissionedDto commissionedDto){
  }
}
