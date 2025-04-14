package com.github.PiotrDuma.payroll.domain.employee.api.model;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import jakarta.validation.Valid;

public class EmployeeRequestDto {

  public record SalariedDto(@Valid Salary salary){
  }

  public record AddSalariedDto(@Valid EmployeeName name,
                               @Valid Address address,
                               @Valid SalariedDto classification
  ){
  }

  public record HourlyDto(@Valid HourlyRate hourlyRate) {
  }

  public record AddHourlyDto(@Valid EmployeeName name,
                             @Valid Address address,
                             @Valid HourlyDto classification
  ){
  }

  public record CommissionedDto(@Valid Salary salary,
                                @Valid CommissionRate commissionedRate) {
  }

  public record AddCommissionedDto(@Valid EmployeeName name,
                                   @Valid Address address,
                                   @Valid CommissionedDto classification
  ){
  }

  public record DirectPaymentMethodDto(@Valid Bank bank,
                                       @Valid BankAccount account){
  }

  public record PaymentMethodDto(@Valid DirectPaymentMethodDto directMethod,
                                 @Valid Address mailMethod){

  }
}
