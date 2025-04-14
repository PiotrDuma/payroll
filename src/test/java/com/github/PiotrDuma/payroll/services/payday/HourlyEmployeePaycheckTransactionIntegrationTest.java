package com.github.PiotrDuma.payroll.services.payday;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.model.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
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
class HourlyEmployeePaycheckTransactionIntegrationTest {
  private static final ZonedDateTime NOW = ZonedDateTime.of(2000, 3, 1,
      16, 5, 12, 0, ZoneId.of("UTC")); //wednesday
  private static final HourlyRate HOURLY_RATE = new HourlyRate(12);
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  private static final Address ADDRESS = new Address("ADDRESS");
  private static final Hours HOURS = new Hours(8d);
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
    this.employeeId = initHourlyEmployee();
  }

  @Test
  void executePaydayShouldInvokePaymentForThreeDays(){
    LocalDate payday = LocalDate.of(2000, 3, 3);
    this.timeCardProvider.addOrUpdateTimeCard(employeeId, NOW.toLocalDate(), HOURS);
    this.timeCardProvider.addOrUpdateTimeCard(employeeId, NOW.toLocalDate().plusDays(1), HOURS);
    this.timeCardProvider.addOrUpdateTimeCard(employeeId, NOW.toLocalDate().plusDays(2), HOURS);

    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(288, paycheck.getSalary().getSalary().doubleValue());
  }

  @Test
  void executePaydayShouldInvokePaymentForTimeCardWithFirstDayOfPaymentPeriod(){
    LocalDate payday = LocalDate.of(2000, 3, 3);
    this.timeCardProvider.addOrUpdateTimeCard(employeeId, NOW.toLocalDate(), HOURS);

    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(96, paycheck.getSalary().getSalary().doubleValue());
  }

  @Test
  void executePaydayShouldInvokePaymentForTimeCardWithEmployeePaydayDate(){
    LocalDate payday = LocalDate.of(2000, 3, 3);
    this.timeCardProvider.addOrUpdateTimeCard(employeeId, payday, HOURS);

    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(96, paycheck.getSalary().getSalary().doubleValue());
  }

  @Test
  void executePaydayShouldInvokePaymentExcludingTimeCardsFromBeforePaymentPeriod(){
    LocalDate payday = LocalDate.of(2000, 3, 3);
    this.timeCardProvider.addOrUpdateTimeCard(employeeId, payday, HOURS);
    this.timeCardProvider.addOrUpdateTimeCard(employeeId, NOW.toLocalDate().minusDays(1), HOURS);
    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(96, paycheck.getSalary().getSalary().doubleValue());
  }

  @Test
  void executePaydayShouldInvokePaymentExcludingTimeCardsFromAfterPaymentPeriod(){
    LocalDate payday = LocalDate.of(2000, 3, 3);
    this.timeCardProvider.addOrUpdateTimeCard(employeeId, payday, HOURS);
    this.timeCardProvider.addOrUpdateTimeCard(employeeId, payday.plusDays(1), HOURS);
    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(96, paycheck.getSalary().getSalary().doubleValue());
  }

  private void executePaydayWithDate(LocalDate payday) {
    PaydayTransactionService modifiedTransaction = (PaydayTransactionService) transaction;
    modifiedTransaction.setToday(payday);
    modifiedTransaction.execute();
  }

  private EmployeeId initHourlyEmployee(){
    return this.employeeFactory.initHourlyEmployeeTransaction(ADDRESS, EMPLOYEE_NAME, HOURLY_RATE)
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
