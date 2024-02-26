package com.github.PiotrDuma.payroll.domain.payment.schedule;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Tag("IntegrationTest")
@ActiveProfiles("test")
class PaymentScheduleIntegrationTest {
  private static final Address ADDRESS = new Address("ADDRESS");
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  private static final Salary SALARY = new Salary(1234);
  private static final HourlyRate HOURLY_RATE = new HourlyRate(12);
  private static final CommissionRate COMMISSION_RATE = new CommissionRate(12.);

  @Autowired
  private AddEmployeeTransactionFactory addEmployeeFactory;

  @Autowired
  private ChangeEmployeeService changeEmployeeService;

  @Autowired
  private ReceiveEmployee repo;

  @Test
  void initSalariedEmployeeTransactionShouldSetMonthlySchedule(){
    EmployeeId employeeId = initEmployee();

    EmployeeResponse employee = repo.find(employeeId);
    assertTrue(employee.getSchedule() instanceof MonthlyPaymentSchedule);
  }

  @Test
  void initHourlyEmployeeTransactionShouldSetWeeklySchedule(){
    EmployeeId employeeId = addEmployeeFactory.initHourlyEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, HOURLY_RATE).execute();

    EmployeeResponse employee = repo.find(employeeId);
    assertTrue(employee.getSchedule() instanceof WeeklyPaymentSchedule);
  }

  @Test
  void initCommissionedEmployeeTransactionShouldSetBiweeklySchedule(){
    EmployeeId employeeId = addEmployeeFactory.initCommissionedEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, SALARY, COMMISSION_RATE).execute();

    EmployeeResponse employee = repo.find(employeeId);
    assertTrue(employee.getSchedule() instanceof BiweeklyPaymentSchedule);
  }

  @Test
  void changeSalariedClassificationTransactionShouldSetMonthlySchedule(){
    EmployeeId employeeId = addEmployeeFactory.initHourlyEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, HOURLY_RATE).execute();

    changeEmployeeService.changeSalariedClassificationTransaction(employeeId, SALARY);

    EmployeeResponse employee = repo.find(employeeId);
    assertTrue(employee.getSchedule() instanceof MonthlyPaymentSchedule);
  }

  @Test
  void changeHourlyClassificationTransactionShouldSetWeeklySchedule(){
    EmployeeId employeeId = initEmployee();

    changeEmployeeService.changeHourlyClassificationTransaction(employeeId, HOURLY_RATE);

    EmployeeResponse employee = repo.find(employeeId);
    assertTrue(employee.getSchedule() instanceof WeeklyPaymentSchedule);
  }

  @Test
  void changeCommissionedClassificationTransactionShouldSetBiweeklySchedule(){

    EmployeeId employeeId = initEmployee();

    changeEmployeeService.changeCommissionedClassificationTransaction(employeeId, SALARY, COMMISSION_RATE);

    EmployeeResponse employee = repo.find(employeeId);
    assertTrue(employee.getSchedule() instanceof BiweeklyPaymentSchedule);
  }

  private EmployeeId initEmployee(){
    AddEmployeeTransaction transaction = addEmployeeFactory.initSalariedEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, SALARY);
    return transaction.execute();
  }
}