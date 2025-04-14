package com.github.PiotrDuma.payroll.domain.employee;

import static com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddCommissionedDto;
import static com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddHourlyDto;
import static com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddSalariedDto;
import static com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.PaymentMethodDto;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.CommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.HourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.SalariedDto;
import com.github.PiotrDuma.payroll.tools.UUIDParser;
import jakarta.validation.Valid;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class EmployeeExtendedController {
  private final Logger log = LoggerFactory.getLogger(EmployeeExtendedController.class);
  private final AddEmployeeTransactionFactory addEmployee;
  private final ChangeEmployeeService changeEmployeeService;

  @Autowired
  public EmployeeExtendedController(AddEmployeeTransactionFactory addEmployee,
      ChangeEmployeeService changeEmployeeService) {
    this.addEmployee = addEmployee;
    this.changeEmployeeService = changeEmployeeService;
  }

  @PutMapping("/employees/{id}/address")
  public ResponseEntity<EmployeeId> changeEmployeeAddress(@PathVariable("id") UUID id,
      @RequestBody @Valid Address address){
    log.debug(String.format("Request PUT performed on '/employees/%s/address' endpoint", id));

    UUID uuid = UUIDParser.parse(id.toString());

    this.changeEmployeeService.changeAddressTransaction(new EmployeeId(uuid), address);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/employees/{id}/name")
  public ResponseEntity<EmployeeId> changeEmployeeName(@PathVariable("id") UUID id,
      @RequestBody @Valid EmployeeName name){
    log.debug(String.format("Request PUT performed on '/employees/%s/name' endpoint", id));

    UUID uuid = UUIDParser.parse(id.toString());

    this.changeEmployeeService.changeNameTransaction(new EmployeeId(uuid), name);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/employees/salaried")
  public ResponseEntity<EmployeeId> addEmployee(@RequestBody @Valid AddSalariedDto dto){
    log.debug("Request POST performed on '/employees/salaried' endpoint");

    EmployeeId id = getAddSalariedEmployeeTransaction(dto).execute();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/employees/" + id);
    return new ResponseEntity<>(id, headers, HttpStatus.CREATED);
  }

  @PutMapping("/employees/{id}/salaried")
  public ResponseEntity<EmployeeId> changePaymentClassification(@PathVariable("id") UUID id,
      @RequestBody @Valid SalariedDto dto){
    log.debug(String.format("Request PUT performed on '/employees/%s/salaried' endpoint", id));

    UUID uuid = UUIDParser.parse(id.toString());

    this.changeEmployeeService.changeSalariedClassificationTransaction(new EmployeeId(uuid), dto.salary());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/employees/hourly")
  public ResponseEntity<EmployeeId> addEmployee(@RequestBody @Valid AddHourlyDto dto){
    log.debug("Request POST performed on '/employees/hourly' endpoint");

    EmployeeId id = getAddHourlyEmployeeTransaction(dto).execute();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/employees/" + id);
    return new ResponseEntity<>(id, headers, HttpStatus.CREATED);
  }

  @PutMapping("/employees/{id}/hourly")
  public ResponseEntity<EmployeeId> changePaymentClassification(@PathVariable("id") UUID id,
      @RequestBody @Valid HourlyDto dto){
    log.debug(String.format("Request PUT performed on '/employees/%s/hourly' endpoint", id));

    UUID uuid = UUIDParser.parse(id.toString());

    this.changeEmployeeService.changeHourlyClassificationTransaction(new EmployeeId(uuid), dto.hourlyRate());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/employees/commissioned")
  public ResponseEntity<EmployeeId> addEmployee(@RequestBody @Valid AddCommissionedDto dto){
    log.debug("Request POST performed on '/employees/salaried' endpoint");

    EmployeeId id = getAddCommissionedEmployeeTransaction(dto).execute();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/employees/" + id);
    return new ResponseEntity<>(id, headers, HttpStatus.CREATED);
  }

  @PutMapping("/employees/{id}/commissioned")
  public ResponseEntity<EmployeeId> changePaymentClassification(@PathVariable("id") UUID id,
      @RequestBody @Valid CommissionedDto dto){
    log.debug(String.format("Request PUT performed on '/employees/%s/commissioned' endpoint", id));

    UUID uuid = UUIDParser.parse(id.toString());

    this.changeEmployeeService.changeCommissionedClassificationTransaction(new EmployeeId(uuid),
        dto.salary(), dto.commissionedRate());
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/employees/{id}/method")
  public ResponseEntity<EmployeeId> changePaymentMethod(@PathVariable("id") UUID id,
      @RequestBody @Valid PaymentMethodDto dto){
    log.debug(String.format("Request PUT performed on '/employees/%s/commissioned' endpoint", id));

    UUID uuid = UUIDParser.parse(id.toString());

    if(dto.directMethod() !=null){
      this.changeEmployeeService.changeDirectPaymentMethodTransaction(
          new EmployeeId(uuid), dto.directMethod().bank(), dto.directMethod().account());
    } else if (dto.mailMethod() != null) {
      this.changeEmployeeService.changeMailPaymentMethodTransaction(new EmployeeId(uuid), dto.mailMethod());
    } else {
      this.changeEmployeeService.changeHoldPaymentMethodTransaction(new EmployeeId(uuid));
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

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
}
