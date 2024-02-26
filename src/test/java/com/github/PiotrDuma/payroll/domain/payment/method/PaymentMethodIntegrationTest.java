package com.github.PiotrDuma.payroll.domain.payment.method;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Tag("IntegrationTest")
@ActiveProfiles("test")
class PaymentMethodIntegrationTest {
  private static final Address ADDRESS = new Address("ADDRESS");
  private static final EmployeeName EMPLOYEE_NAME = new EmployeeName("NAME");
  private static final Salary SALARY = new Salary(1234);

  @Autowired
  private AddEmployeeTransactionFactory addEmployeeFactory;

  @Autowired
  private ChangeEmployeeService changeEmployeeService;

  @Autowired
  private ReceiveEmployee repo;

  @Test
  void changeEmployeeServiceShouldSetDirectPaymentMethod(){
    Bank bank = new Bank("bank");
    BankAccount bankAccount = new BankAccount("01234567980123456789012345");
    EmployeeId employeeId = initEmployee();

    changeEmployeeService.changeDirectPaymentMethodTransaction(employeeId, bank, bankAccount);

    EmployeeResponse employee = repo.find(employeeId);
    assertTrue(employee.getPaymentMethod() instanceof DirectPaymentMethod);

    DirectPaymentMethod method = (DirectPaymentMethod) employee.getPaymentMethod();

    assertEquals(bank, method.getBank());
    assertEquals(bankAccount, method.getBankAccount());
  }

  @Test
  void changeEmployeeServiceShouldSetMailPaymentMethod(){
    Address address = new Address("new address");

    EmployeeId employeeId = initEmployee();

    changeEmployeeService.changeMailPaymentMethodTransaction(employeeId, address);

    EmployeeResponse employee = repo.find(employeeId);
    assertTrue(employee.getPaymentMethod() instanceof MailPaymentMethod);

    MailPaymentMethod method = (MailPaymentMethod) employee.getPaymentMethod();

    assertEquals(address, method.getAddress());
  }

  @Test
  void changeEmployeeServiceShouldHoldPaymentMethod(){
    Address address = new Address("new address");
    EmployeeId employeeId = initEmployee();

    changeEmployeeService.changeMailPaymentMethodTransaction(employeeId, address);

    EmployeeResponse employee = repo.find(employeeId);
    assertTrue(employee.getPaymentMethod() instanceof MailPaymentMethod);

    changeEmployeeService.changeHoldPaymentMethodTransaction(employeeId);

    EmployeeResponse finalEmployee = repo.find(employeeId);

    assertTrue(finalEmployee.getPaymentMethod() instanceof HoldPaymentMethod);
  }

  private EmployeeId initEmployee(){
    AddEmployeeTransaction transaction = addEmployeeFactory.initSalariedEmployeeTransaction(
        ADDRESS, EMPLOYEE_NAME, SALARY);
    return transaction.execute();
  }
}