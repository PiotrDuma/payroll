package com.github.PiotrDuma.payroll.tools;

import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface LocalDateParser {
  String EXCEPTION = "Invalid data value. Format must be: yyyy-mm-dd";
  static LocalDate parseDate(String date){
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    LocalDate parsed;
    try{
      parsed = LocalDate.parse(date, formatter);
    }catch (Exception e){
      throw new InvalidArgumentException(EXCEPTION);
    }
    return parsed;
  }
}
