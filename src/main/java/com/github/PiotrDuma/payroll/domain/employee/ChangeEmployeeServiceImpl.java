package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class ChangeEmployeeServiceImpl implements ChangeEmployeeService {
  private final ChangeEmployeeTransactionFactory factory;

  @Autowired
  public ChangeEmployeeServiceImpl(ChangeEmployeeTransactionFactory factory) {
    this.factory = factory;
  }

  @Override
  public void changeNameTransaction(EmployeeId employeeId, EmployeeName name) {
    this.factory.create(ChangeEmployeeTransaction.NAME, employeeId, name)
        .execute();
  }

  @Override
  public void changeAddressTransaction(EmployeeId employeeId, Address address) {
    this.factory.create(ChangeEmployeeTransaction.ADDRESS, employeeId, address)
        .execute();
  }

  @Override
  public void changeHourlyClassificationTransaction(EmployeeId employeeId, HourlyRate hourlyRate) {
    this.factory.create(ChangeEmployeeTransaction.HOURLY_CLASSIFICATION, employeeId, hourlyRate)
        .execute();
  }

  @Override
  public void changeSalariedClassificationTransaction(EmployeeId employeeId, Salary salary) {
    this.factory.create(ChangeEmployeeTransaction.SALARIED_CLASSIFICATION, employeeId, salary)
        .execute();
  }

  @Override
  public void changeCommissionedClassificationTransaction(EmployeeId employeeId, Salary salary,
      CommissionRate rate) {
    this.factory.create(ChangeEmployeeTransaction.COMMISSIONED_CLASSIFICATION, employeeId, salary, rate)
        .execute();
  }

  @Override
  public void changeHoldPaymentMethodTransaction(EmployeeId employeeId) {
    this.factory.create(ChangeEmployeeTransaction.HOLD_PAYMENT, employeeId)
        .execute();
  }

  @Override
  public void changeDirectPaymentMethodTransaction(EmployeeId employeeId, Bank bank,
      BankAccount account) {
    this.factory.create(ChangeEmployeeTransaction.DIRECT_PAYMENT, employeeId, bank, account)
        .execute();
  }

  @Override
  public void changeMailPaymentMethodTransaction(EmployeeId employeeId, Address address) {
    this.factory.create(ChangeEmployeeTransaction.MAIL_PAYMENT, employeeId, address)
        .execute();
  }
}
