package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import com.github.PiotrDuma.payroll.tools.UUIDParser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UnionController {
  private static final Logger log = LoggerFactory.getLogger(UnionController.class);
  private static final String NOT_FOUND = "Union '%s' not found";
  private static final String MESSAGE_REVOKE_MEMBERSHIP = "POST request on '/unions/{id}/undo' with"
      + " parameters id=%s, EmployeeDto=%s executed at %s";
  private static final String MESSAGE_ADD_MEMBER = "POST request on '/unions/{id}/add' with"
      + " parameters id=%s, EmployeeDto=%s executed at %s";
  private static final String MESSAGE_CHARGE = "POST request on '/unions/{id}/{amount}' with"
      + " parameters id=%s, amount=%s executed at %s";
  private static final String MESSAGE_ADD_UNION = "POST request on '/unions/add' with"
      + " parameters unionNameDto=%s executed at %s";
  private final UnionAffiliationRepository repo;
  private final UnionAffiliationService unionService;
  private final Clock clock;

  @Autowired
  public UnionController(UnionAffiliationRepository repo, UnionAffiliationService unionService,
      Clock clock) {
    this.repo = repo;
    this.unionService = unionService;
    this.clock = clock;
  }

  @PostMapping("/unions")
  public ResponseEntity<UnionDto> addUnion(@RequestBody @Valid UnionNameDto unionName){
    log.info(String.format(MESSAGE_ADD_UNION, unionName, this.clock.instant().toString()));
    UnionDto unionDto = this.unionService.addUnion(unionName.name());
    return new ResponseEntity<>(unionDto, HttpStatus.OK);
  }

  @GetMapping("/unions")
  public ResponseEntity<List<UnionDto>> getUnions(){
    List<UnionDto> collection = repo.findAll().stream().map(UnionEntity::toDto)
        .collect(Collectors.toList());
    return new ResponseEntity<>(collection, HttpStatus.OK);
  }

  @GetMapping("/unions/{id}")
  public ResponseEntity<UnionDto> getUnions(@PathVariable("id") String id){
    UUID parsedId = UUIDParser.parse(id);
    UnionEntity union = repo.findById(parsedId)
        .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND, id)));
    return new ResponseEntity<>(union.toDto(), HttpStatus.OK);
  }

  @PostMapping(value = "/unions/{id}/charge")
  public ResponseEntity<UnionDto> charge(@PathVariable("id") String id,
       @Valid @RequestBody AmountDto amount){
    log.info(String.format(MESSAGE_CHARGE, id, amount.toString(), this.clock.instant().toString()));

    LocalDate date = LocalDate.ofInstant(clock.instant(), clock.getZone());
    this.unionService.chargeMembers(UUIDParser.parse(id), new Amount(amount.amount()), date);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/unions/{id}/add")
  public ResponseEntity<UnionDto> addMember(@PathVariable("id") String id,
      @RequestBody @Valid EmployeeDto employee){
    log.info(String.format(MESSAGE_ADD_MEMBER, id, employee.toString(),
        this.clock.instant().toString()));
    this.unionService.recordMembership(UUIDParser.parse(id),
        new EmployeeId(UUIDParser.parse(employee.id())));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/unions/{id}/undo")
  public ResponseEntity<UnionDto> revokeMembership(@PathVariable("id") String id,
      @RequestBody @Valid EmployeeDto employee){
    log.info(String.format(MESSAGE_REVOKE_MEMBERSHIP, id, employee.toString(),
        this.clock.instant().toString()));

    this.unionService.undoMembershipAffiliation(UUIDParser.parse(id),
        new EmployeeId(UUIDParser.parse(employee.id())));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private record UnionNameDto(@NotBlank String name) {
    private UnionNameDto(String name) {
        this.name = name;
      }

    @Override
    public String name() {
      return name;
    }
  }

  private record AmountDto(@NotNull(message = "Amount cannot empty")
                           @DecimalMin(value = "0.0", message = "Amount cannot be lower than 0") Double amount) {
    private AmountDto(Double amount) {
        this.amount = amount;
      }

    @Override
    public Double amount() {
      return amount;
    }
  }

  private record EmployeeDto(@NotBlank String id) {
    private EmployeeDto(String id) {
        this.id = id;
      }
    }
}
