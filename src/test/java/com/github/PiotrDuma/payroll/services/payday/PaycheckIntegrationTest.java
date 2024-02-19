package com.github.PiotrDuma.payroll.services.payday;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Tag("IntegrationTest")
class PaycheckIntegrationTest {
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());
  private static final Salary SALARY = new Salary(1234);
  private static final   Amount UNION_DUES = new Amount(234);
  private static final LocalDate DATE = LocalDate.of(2000, 1, 1);

  @Autowired
  private PaycheckRepository repo;

  private PaycheckEntity paycheck;

  @BeforeEach
  void setUp(){
    this.paycheck = new PaycheckEntity(EMPLOYEE_ID, SALARY, UNION_DUES, DATE);
  }

  @Test
  void shouldSaveEntityToRepository(){
    PaycheckEntity saved = this.repo.save(paycheck);

    assertEquals(paycheck.getId(), saved.getId());
    assertEquals(paycheck.getSalary(), saved.getSalary());
    assertEquals(paycheck.getNetSalary(), saved.getNetSalary());
    assertEquals(paycheck.getUnionDues(), saved.getUnionDues());
    assertEquals(paycheck.getEmployeeId(), saved.getEmployeeId());
    assertEquals(paycheck.getDate(), saved.getDate());
    assertEquals(paycheck, saved);
  }

  @Test
  void shouldReturnAllPaychecks(){
    PaycheckEntity paycheck2 = new PaycheckEntity(EMPLOYEE_ID, SALARY, UNION_DUES, DATE);

    PaycheckEntity saved = this.repo.save(paycheck);
    PaycheckEntity saved2 = this.repo.save(paycheck2);

    assertEquals(2, this.repo.findAll().size());
    assertTrue(this.repo.findById(saved.getId()).isPresent());
    assertTrue(this.repo.findById(saved2.getId()).isPresent());
  }

  @Test
  void shouldReturnPaychecksByEmployeeId(){
    PaycheckEntity paycheck2 = new PaycheckEntity(new EmployeeId(UUID.randomUUID()), SALARY, UNION_DUES, DATE);

    PaycheckEntity saved = this.repo.save(paycheck);
    PaycheckEntity saved2 = this.repo.save(paycheck2);

    assertEquals(2, this.repo.findAll().size());
    assertEquals(1, this.repo.findAllByEmployeeId(EMPLOYEE_ID).size());
    assertTrue(this.repo.findAllByEmployeeId(EMPLOYEE_ID).contains(saved));
  }
}