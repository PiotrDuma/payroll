package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardResponseDto;
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
class TimeCardController {
  private final Logger log = LoggerFactory.getLogger(TimeCardController.class);
  private static final String DATE_NOT_NULL = "Date cannot be null.";
  private final TimeCardProvider timeCard;

  @Autowired
  public TimeCardController(TimeCardProvider timeCard) {
    this.timeCard = timeCard;
  }

  //Populate response with provided input data
  //Refactor time card service if more features are required.
  @PostMapping(value = "/employees/{id}/timecard", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TimeCardResponseDto> createTimeCard(@PathVariable("id") String id,
      @RequestBody @Valid TimeCardRequestDto dto){
    log.debug(String.format("Request POST performed on '/employees/{%s}/timecard' endpoint", id));

    EmployeeId parsedId = new EmployeeId(UUIDParser.parse(id));
    LocalDate parsedDate = LocalDateParser.parseDate(dto.date());
    this.timeCard.addOrUpdateTimeCard(parsedId, parsedDate, dto.hours());

    TimeCardResponseDto response = new TimeCardResponseDto(parsedId, parsedDate, dto.hours());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  public record TimeCardRequestDto(@NotNull(message = DATE_NOT_NULL) String date,
                                   @Valid Hours hours
  ){
  }
}
