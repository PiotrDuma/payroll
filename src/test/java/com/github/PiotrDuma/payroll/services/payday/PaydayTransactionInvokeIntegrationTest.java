package com.github.PiotrDuma.payroll.services.payday;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
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
class PaydayTransactionInvokeIntegrationTest { //TODO: implement transaction integration test.
  private static final ZonedDateTime NOW = ZonedDateTime.of(2000, 3, 1,
      16, 5, 12, 0, ZoneId.of("UTC")); //wednesday
  private static final Salary SALARY = new Salary(1234);
  private static final HourlyRate HOURLY_RATE = new HourlyRate(12);
  private static final CommissionRate COMMISSION_RATE = new CommissionRate(12);
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  private static final Address ADDRESS = new Address("ADDRESS");
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

  private PaydayTransaction transaction;

  private EmployeeId salariedEmployee;
  private EmployeeId hourlyEmployee;
  private EmployeeId commissionedEmployee;

  @BeforeEach
  void setUp(){
    this.transaction = new PaydayTransactionService(receiveEmployee, unionAffiliationService,
        clock, paycheckRepository);
    this.salariedEmployee = initSalariedEmployee();
    this.hourlyEmployee = initHourlyEmployee();
    this.commissionedEmployee = initCommissionedEmployee();
  }

  @Test
  void executePaydayShouldNotInvokeAnyPayment(){
    LocalDate payday = LocalDate.of(2000, 3, 2); //thursday
    executePaydayWithDate(payday);

    assertEquals(0, paycheckRepository.findAll().size());
  }

  @Test
  void executePaydayShouldInvokePaymentOnlyForHourlyEmployee(){
    LocalDate payday = LocalDate.of(2000, 3, 3);
    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(hourlyEmployee, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
  }

//  @Test //TODO: fix salesReceipt
//  void executePaydayShouldInvokePaymentForWeeklyAndBiweeklyEmployees(){
//    LocalDate payday = LocalDate.of(2000, 3, 10);
//    executePaydayWithDate(payday);
//
//    assertEquals(2, paycheckRepository.findAll().size());
//    assertEquals(1, paycheckRepository.findAllByEmployeeId(hourlyEmployee).size());
//    assertEquals(1, paycheckRepository.findAllByEmployeeId(commissionedEmployee).size());
//  }

  @Test
  void executePaydayShouldInvokePaymentForMonthlyEmployee(){
    LocalDate payday = LocalDate.of(2000, 4, 30); //sunday
    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    assertEquals(1, paycheckRepository.findAllByEmployeeId(salariedEmployee).size());
  }

//  @Test //TODO: fix salesReceipt
//  void executePaydayShouldInvokePaymentForAllEmployees(){
//    LocalDate payday = LocalDate.of(2000, 3, 31);
//    executePaydayWithDate(payday);
//
//    assertEquals(3, paycheckRepository.findAll().size());
//    assertEquals(1, paycheckRepository.findAllByEmployeeId(hourlyEmployee).size());
//    assertEquals(1, paycheckRepository.findAllByEmployeeId(commissionedEmployee).size());
//    assertEquals(1, paycheckRepository.findAllByEmployeeId(salariedEmployee).size());
//  }

  private void executePaydayWithDate(LocalDate payday) {
    PaydayTransactionService modifiedTransaction = (PaydayTransactionService) transaction;
    modifiedTransaction.setToday(payday);
    modifiedTransaction.execute();
  }

  private EmployeeId initSalariedEmployee(){
    return this.employeeFactory.initSalariedEmployeeTransaction(ADDRESS,
            EMPLOYEE_NAME, SALARY)
         .execute();
  }

  private EmployeeId initHourlyEmployee(){
    return this.employeeFactory.initHourlyEmployeeTransaction(ADDRESS, EMPLOYEE_NAME, HOURLY_RATE)
        .execute();
  }

  private EmployeeId initCommissionedEmployee(){
    return this.employeeFactory.initCommissionedEmployeeTransaction(ADDRESS, EMPLOYEE_NAME, SALARY,
            COMMISSION_RATE)
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