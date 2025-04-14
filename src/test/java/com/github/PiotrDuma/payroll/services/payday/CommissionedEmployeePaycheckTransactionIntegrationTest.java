package com.github.PiotrDuma.payroll.services.payday;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.model.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
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
class CommissionedEmployeePaycheckTransactionIntegrationTest {
  private static final ZonedDateTime NOW = ZonedDateTime.of(2000, 3, 1,
      16, 5, 12, 0, ZoneId.of("UTC")); //wednesday
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  private static final Address ADDRESS = new Address("ADDRESS");
  private static final Salary SALARY = new Salary(1000);
  private static final Amount AMOUNT = new Amount(1000);
  private static final CommissionRate COMMISSION_RATE = new CommissionRate(15);
  @Autowired private AddEmployeeTransactionFactory employeeFactory;
  @Autowired private ReceiveEmployee receiveEmployee;
  @Autowired private UnionAffiliationService unionAffiliationService;
  @Autowired private PaycheckRepository paycheckRepository;
  @Autowired private Clock clock;
  @Autowired private SalesReceiptProvider salesReceiptProvider;
  private PaydayTransaction transaction;
  private EmployeeId employeeId;

  @BeforeEach
  void setUp(){
    this.transaction = new PaydayTransactionService(receiveEmployee, unionAffiliationService,
        clock, paycheckRepository);
    this.employeeId = initCommissionedEmployee();
  }

  @Test
  void executePaydayShouldInvokePaymentWithSalesReceiptsOnPeriodBoundaries(){
    double expectedSalary = 1000 + 2 * 150;
    LocalDate payday = LocalDate.of(2000, 3, 10);

    this.salesReceiptProvider.addSalesReceipt(employeeId, NOW.toLocalDate(),AMOUNT);
    this.salesReceiptProvider.addSalesReceipt(employeeId, payday, AMOUNT);
    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(expectedSalary, paycheck.getNetSalary().getSalary().doubleValue());
  }

  @Test
  void executePaydayShouldInvokePaymentWithoutSalesReceiptsOutOfPeriod(){
    double expectedSalary = SALARY.getSalary().doubleValue();
    LocalDate payday = LocalDate.of(2000, 3, 10);

    this.salesReceiptProvider.addSalesReceipt(employeeId, NOW.toLocalDate().minusDays(1) ,AMOUNT);
    this.salesReceiptProvider.addSalesReceipt(employeeId, payday.plusDays(1), AMOUNT);
    executePaydayWithDate(payday);

    assertEquals(1, paycheckRepository.findAll().size());
    PaycheckEntity paycheck = paycheckRepository.findAll().iterator().next();

    assertEquals(employeeId, paycheck.getEmployeeId());
    assertEquals(payday, paycheck.getDate());
    assertEquals(0, paycheck.getUnionDues().getAmount().doubleValue());
    assertEquals(expectedSalary, paycheck.getNetSalary().getSalary().doubleValue());
  }

  private void executePaydayWithDate(LocalDate payday) {
    PaydayTransactionService modifiedTransaction = (PaydayTransactionService) transaction;
    modifiedTransaction.setToday(payday);
    modifiedTransaction.execute();
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
