package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.common.Address;
import com.github.PiotrDuma.payroll.common.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.salary.api.SalariedClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ChangeEmployeeTransactionFactoryTest {
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());

  @Mock private EmployeeRepository repo;
  @Mock private PaymentMethodFactory methodFactory;
  @Mock private PaymentScheduleFactory scheduleFactory;
  @Mock private SalariedClassification salariedClassification;
  @Mock private HourlyClassification hourlyClassification;
  @Mock private CommissionedClassification commissionedClassification;

  private ChangeEmployeeTransactionFactory factory;

  @BeforeEach
  void setUp(){
    this.factory = new ChangeEmployeeTransactionFactory(repo, methodFactory, scheduleFactory,
        salariedClassification, hourlyClassification, commissionedClassification);
  }

  @Test
  void shouldReturnChangeNameTransaction(){
    EmployeeName name = new EmployeeName("NAME");
    int transactionCode = ChangeEmployeeTransaction.NAME;

    ChangeEmployeeTransaction result = this.factory.create(transactionCode, EMPLOYEE_ID, name);

    assertTrue(result instanceof ChangeNameTransaction);
    assertEquals(name, ((ChangeNameTransaction) result).getName());
    assertEquals(EMPLOYEE_ID, ((ChangeNameTransaction) result).getEmployeeId());
  }

  @Test
  void shouldReturnChangeAddressTransaction(){
    Address address = new Address("ADDRESS");
    int transactionCode = ChangeEmployeeTransaction.ADDRESS;

    ChangeEmployeeTransaction result = this.factory.create(transactionCode, EMPLOYEE_ID, address);

    assertTrue(result instanceof ChangeAddressTransaction);
    assertEquals(address, ((ChangeAddressTransaction) result).getAddress());
    assertEquals(EMPLOYEE_ID, ((ChangeAddressTransaction) result).getEmployeeId());
  }

  @Test
  void shouldReturnChangeHourlyClassificationTransaction(){
    HourlyRate hourlyRate = new HourlyRate(12.5);
    int transactionCode = ChangeEmployeeTransaction.HOURLY_CLASSIFICATION;

    ChangeEmployeeTransaction result = this.factory.create(transactionCode, EMPLOYEE_ID, hourlyRate);

    assertTrue(result instanceof ChangeHourlyClassificationTransaction);
    assertEquals(hourlyRate, ((ChangeHourlyClassificationTransaction) result).getHourlyRate());
    assertEquals(EMPLOYEE_ID, ((ChangeHourlyClassificationTransaction) result).getEmployeeId());
  }

  @Test
  void shouldReturnChangeSalariedClassificationTransaction(){
    Salary salary = new Salary(1234);
    int transactionCode = ChangeEmployeeTransaction.SALARIED_CLASSIFICATION;

    ChangeEmployeeTransaction result = this.factory.create(transactionCode, EMPLOYEE_ID, salary);

    assertTrue(result instanceof ChangeSalariedClassificationTransaction);
    assertEquals(salary, ((ChangeSalariedClassificationTransaction) result).getSalary());
    assertEquals(EMPLOYEE_ID, ((ChangeSalariedClassificationTransaction) result).getEmployeeId());
  }

  @Test
  void shouldReturnChangeCommissionedClassificationTransaction(){
    Salary salary = new Salary(1234);
    CommissionRate commissionRate = new CommissionRate(12.5);
    int transactionCode = ChangeEmployeeTransaction.COMMISSIONED_CLASSIFICATION;

    ChangeEmployeeTransaction result = this.factory.create(transactionCode, EMPLOYEE_ID,
        salary, commissionRate);

    assertTrue(result instanceof ChangeCommissionedClassificationTransaction);
    assertEquals(salary, ((ChangeCommissionedClassificationTransaction) result).getSalary());
    assertEquals(commissionRate, ((ChangeCommissionedClassificationTransaction) result).getCommissionRate());
    assertEquals(EMPLOYEE_ID, ((ChangeCommissionedClassificationTransaction) result).getEmployeeId());
  }

  @Test
  void shouldReturnChangeHoldPaymentMethodTransaction(){
    int transactionCode = ChangeEmployeeTransaction.HOLD_PAYMENT;

    ChangeEmployeeTransaction result = this.factory.create(transactionCode, EMPLOYEE_ID);

    assertTrue(result instanceof ChangeHoldMethodTransaction);
    assertEquals(EMPLOYEE_ID, ((ChangeHoldMethodTransaction) result).getEmployeeId());
  }

  @Test
  void shouldReturnChangeDirectPaymentMethodTransaction(){
    Bank bank = new Bank("BANK NAME");
    BankAccount account = new BankAccount("01234567890123456789012345");
    int transactionCode = ChangeEmployeeTransaction.DIRECT_PAYMENT;

    ChangeEmployeeTransaction result = this.factory.create(transactionCode, EMPLOYEE_ID, bank, account);

    assertTrue(result instanceof ChangeDirectMethodTransaction);
    assertEquals(bank, ((ChangeDirectMethodTransaction) result).getBank());
    assertEquals(account, ((ChangeDirectMethodTransaction) result).getBankAccount());
    assertEquals(EMPLOYEE_ID, ((ChangeDirectMethodTransaction) result).getEmployeeId());
  }

  @Test
  void shouldReturnChangeMailPaymentMethodTransaction(){
    Address address = new Address("address");
    int transactionCode = ChangeEmployeeTransaction.MAIL_PAYMENT;

    ChangeEmployeeTransaction result = this.factory.create(transactionCode, EMPLOYEE_ID, address);

    assertTrue(result instanceof ChangeMailMethodTransaction);
    assertEquals(address, ((ChangeMailMethodTransaction) result).getAddress());
    assertEquals(EMPLOYEE_ID, ((ChangeMailMethodTransaction) result).getEmployeeId());
  }
}