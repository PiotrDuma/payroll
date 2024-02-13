package com.github.PiotrDuma.payroll.services.payday;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.common.Amount;
import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.common.Salary;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PaycheckEntityTest {

  @Test
  void shouldCalculateValidNetSalary(){
    BigDecimal expected = new BigDecimal(100);
    EmployeeId employeeId = new EmployeeId(UUID.randomUUID());
    Amount unionDues = new Amount(23);
    Salary salary = new Salary(123);
    LocalDate date = LocalDate.of(2000,1,1);

    PaycheckEntity paycheck = new PaycheckEntity(employeeId, salary, unionDues, date);

    assertEquals(expected, paycheck.getNetSalary().getSalary());
  }
}