package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptResponseDto;
import com.github.PiotrDuma.payroll.tools.LocalDateParser;
import com.github.PiotrDuma.payroll.tools.UUIDParser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SalesReceiptController {
  private final Logger log = LoggerFactory.getLogger(SalesReceiptController.class);
  private static final String DATE_NOT_NULL = "Date cannot be null.";

  private final SalesReceiptProvider service;

  @Autowired
  public SalesReceiptController(SalesReceiptProvider service) {
    this.service = service;
  }

  @PostMapping(value = "/employees/{id}/receipts", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SalesReceiptResponseDto> postReceipt(@PathVariable("id") String id,
      @RequestBody @Valid SalesReceiptRequestDto dto){
    log.debug(String.format("Request POST performed on '/employees/%s/receipts' endpoint", id));

    EmployeeId parsedId = new EmployeeId(UUIDParser.parse(id));
    LocalDate parsedDate = LocalDateParser.parseDate(dto.date());
    this.service.addSalesReceipt(parsedId, parsedDate, dto.amount());

    SalesReceiptResponseDto response = new SalesReceiptResponseDto(parsedId, parsedDate, dto.amount());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  public record SalesReceiptRequestDto(@NotNull(message = DATE_NOT_NULL) String date,
                                   @NotNull @Valid Amount amount
  ){
  }
}
