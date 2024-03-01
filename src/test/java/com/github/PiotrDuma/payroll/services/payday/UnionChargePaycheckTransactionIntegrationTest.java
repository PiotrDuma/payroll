package com.github.PiotrDuma.payroll.services.payday;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
import com.github.PiotrDuma.payroll.services.payday.api.PaydayTransaction;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Tag("IntegrationTest")
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.ANY)
class UnionChargePaycheckTransactionIntegrationTest {
  private static final ZonedDateTime NOW = ZonedDateTime.of(2000, 3, 1,
      16, 5, 12, 0, ZoneId.of("UTC")); //wednesday
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  private static final Address ADDRESS = new Address("ADDRESS");
  private static final Salary SALARY = new Salary(1234);
  @Autowired
  private AddEmployeeTransactionFactory employeeFactory;
  @Autowired
  private ReceiveEmployee receiveEmployee;
  @Autowired
  private UnionAffiliationService unionAffiliationService;
  @Autowired
  private PaycheckRepository paycheckRepository;
  @Autowired
  private Clock clock;
  @Autowired
  private TimeCardProvider timeCardProvider;

  private PaydayTransaction transaction;
  private EmployeeId employeeId;

  @BeforeEach
  void setUp(){
    this.transaction = new PaydayTransactionService(receiveEmployee, unionAffiliationService,
        clock, paycheckRepository);
    this.employeeId = initEmployee();
  }

  @Test
  void executePaydayShouldChargeUnionDuesChargedOnTheFirstDayOfPaymentPeriod(){
    double expectedPayment = 1234 - 15;
    LocalDate payday = LocalDate.of(2000, 3, 31);
    addEmployeeToUnionAndChargeMembersOnDate(employeeId, NOW.toLocalDate());

    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(15, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(expectedPayment, paycheck.getNetSalary().getSalary().doubleValue());
  }

  @Test
  void executePaydayShouldChargeUnionDuesChargedOnTheLastDayOfPaymentPeriod(){
    double expectedPayment = 1234 - 15;
    LocalDate payday = LocalDate.of(2000, 3, 31);
    addEmployeeToUnionAndChargeMembersOnDate(employeeId, payday);

    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(15, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(expectedPayment, paycheck.getNetSalary().getSalary().doubleValue());
  }

  @Test
  void executePaydayShouldChargeUnionDuesFromDifferentUnions(){
    double expectedPayment = 1234 - 15 -15;
    LocalDate payday = LocalDate.of(2000, 3, 31);
    addEmployeeToUnionAndChargeMembersOnDate(employeeId, payday);
    addEmployeeToUnionAndChargeMembersOnDate(employeeId, payday);

    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(30, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(expectedPayment, paycheck.getNetSalary().getSalary().doubleValue());
  }

  @Test
  void executePaydayShouldNotChargeUnionDuesFromUnaffiliatedEmployee(){
    double expectedPayment = 1234;
    LocalDate payday = LocalDate.of(2000, 3, 31);

    UnionDto union = this.unionAffiliationService.addUnion("unionname");
    this.unionAffiliationService.chargeMembers(union.id(), new Amount(15), payday);

    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(expectedPayment, paycheck.getNetSalary().getSalary().doubleValue());
  }

  @Test
  void executePaydayShouldNotChargeUnionDuesFromOutOfThePeriod(){
    double expectedPayment = 1234;
    LocalDate payday = LocalDate.of(2000, 3, 31);
    addEmployeeToUnionAndChargeMembersOnDate(employeeId, NOW.toLocalDate().minusDays(1));

    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(expectedPayment, paycheck.getNetSalary().getSalary().doubleValue());
  }

  private void addEmployeeToUnionAndChargeMembersOnDate(EmployeeId employeeId, LocalDate date){
    UnionDto union = this.unionAffiliationService.addUnion("unionname");
    this.unionAffiliationService.recordMembership(union.id(), employeeId);
    this.unionAffiliationService.chargeMembers(union.id(), new Amount(15), date);
  }

  private void executePaydayWithDate(LocalDate payday) {
    PaydayTransactionService modifiedTransaction = (PaydayTransactionService) transaction;
    modifiedTransaction.setToday(payday);
    modifiedTransaction.execute();
  }

  private EmployeeId initEmployee(){
    return this.employeeFactory.initSalariedEmployeeTransaction(ADDRESS, EMPLOYEE_NAME, SALARY)
        .execute();
  }

  @TestConfiguration
  static class TestOverridingClockServiceConfiguration {
    @Bean
    public Clock clock(){
      return Clock.fixed(NOW.toInstant(), NOW.getZone());
    }
  }
}
