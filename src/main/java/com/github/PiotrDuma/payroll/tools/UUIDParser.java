package com.github.PiotrDuma.payroll.tools;

import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import java.util.UUID;
import org.apache.coyote.BadRequestException;

public interface UUIDParser {
  String EXCEPTION = "'%s' is not UUID format.";

  static UUID parse(String id) {
    try{
      return UUID.fromString(id);
    }catch (Exception e){
      throw new InvalidArgumentException(String.format(EXCEPTION, id));
    }
  }
}
