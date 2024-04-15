package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private static final String NOT_FOUND = "Union '%s' not found";
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
    UnionEntity union = repo.findById(UUID.fromString(id))
        .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND, id)));
    return new ResponseEntity<>(union.toDto(), HttpStatus.OK);
  }

  @PostMapping("/unions/{id}/{amount}")
  public ResponseEntity<UnionDto> charge(@PathVariable("id") String id,
      @PathVariable("amount") Double amount){
    LocalDate date = LocalDate.ofInstant(clock.instant(), clock.getZone());
    this.unionService.chargeMembers(UUID.fromString(id), new Amount(amount), date);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/unions/{id}/add")
  public ResponseEntity<UnionDto> addMember(@PathVariable("id") String id,
      @RequestBody @Valid EmployeeDto employee){
    this.unionService.recordMembership(UUID.fromString(id),
        new EmployeeId(UUID.fromString(employee.id())));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/unions/{id}/undo")
  public ResponseEntity<UnionDto> revokeMembership(@PathVariable("id") String id,
      @RequestBody @Valid EmployeeDto employee){
    this.unionService.undoMembershipAffiliation(UUID.fromString(id),
        new EmployeeId(UUID.fromString(employee.id())));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private record UnionNameDto(@NotNull @NotBlank String name) {
    private UnionNameDto(String name) {
        this.name = name;
      }

    @Override
    public String name() {
      return name;
    }
  }

  private record EmployeeDto(@NotNull @NotBlank String id){
    private EmployeeDto(String id){
      this.id = id;
    }
    @Override
    public String id(){
      return id;
    }
  }
}
