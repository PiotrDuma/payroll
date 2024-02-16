package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.BankAccount.BankAccount;
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
import org.springframework.stereotype.Component;

@Component
class ChangeEmployeeTransactionFactory {
  private final EmployeeRepository repo;
  private final PaymentMethodFactory methodFactory;
  private final PaymentScheduleFactory scheduleFactory;
  private final SalariedClassification salariedClassification;
  private final HourlyClassification hourlyClassification;
  private final CommissionedClassification commissionedClassification;

  public ChangeEmployeeTransactionFactory(EmployeeRepository repo,
      PaymentMethodFactory methodFactory,
      PaymentScheduleFactory scheduleFactory, SalariedClassification salariedClassification,
      HourlyClassification hourlyClassification,
      CommissionedClassification commissionedClassification) {
    this.repo = repo;
    this.methodFactory = methodFactory;
    this.scheduleFactory = scheduleFactory;
    this.salariedClassification = salariedClassification;
    this.hourlyClassification = hourlyClassification;
    this.commissionedClassification = commissionedClassification;
  }

  public ChangeEmployeeTransaction create(int transactionCode, Object... params){
    return switch(transactionCode){
      case ChangeEmployeeTransaction.NAME ->
          new ChangeNameTransaction(repo, (EmployeeId)params[0], (EmployeeName)params[1]);
      case ChangeEmployeeTransaction.ADDRESS ->
          new ChangeAddressTransaction(repo, (EmployeeId)params[0], (Address)params[1]);
      case ChangeEmployeeTransaction.HOURLY_CLASSIFICATION ->
          new ChangeHourlyClassificationTransaction(repo, hourlyClassification, scheduleFactory,
              (EmployeeId)params[0], (HourlyRate)params[1]);
      case ChangeEmployeeTransaction.SALARIED_CLASSIFICATION ->
          new ChangeSalariedClassificationTransaction(repo, salariedClassification, scheduleFactory,
              (EmployeeId)params[0], (Salary)params[1]);
      case ChangeEmployeeTransaction.COMMISSIONED_CLASSIFICATION ->
          new ChangeCommissionedClassificationTransaction(repo, commissionedClassification,
              scheduleFactory, (EmployeeId)params[0], (Salary)params[1], (CommissionRate)params[2]);
      case ChangeEmployeeTransaction.HOLD_PAYMENT ->
          new ChangeHoldMethodTransaction(repo, methodFactory, (EmployeeId)params[0]);
      case ChangeEmployeeTransaction.DIRECT_PAYMENT -> new ChangeDirectMethodTransaction(repo,
          methodFactory, (EmployeeId)params[0], (Bank)params[1], (BankAccount)params[2]);
      case ChangeEmployeeTransaction.MAIL_PAYMENT -> new ChangeMailMethodTransaction(repo,
          methodFactory, (EmployeeId)params[0], (Address)params[1]);
      default -> null;
    };
  }
}
