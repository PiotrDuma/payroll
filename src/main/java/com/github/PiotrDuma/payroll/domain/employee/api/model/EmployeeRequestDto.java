package com.github.PiotrDuma.payroll.domain.employee.api.model;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import jakarta.validation.Valid;

public class EmployeeRequestDto {

  public record SalariedDto(@Valid Address address,
                            @Valid EmployeeName name,
                            @Valid Salary salary){
  }
  public record HourlyDto(@Valid Address address,
                          @Valid EmployeeName name,
                          @Valid HourlyRate hourlyRate) {
  }
  public record CommissionedDto(@Valid Address address,
                                @Valid EmployeeName name,
                                @Valid Salary salary,
                                @Valid CommissionRate commissionedRate) {
  }

  public record DirectPaymentMethodDto(@Valid Bank bank,
                                       @Valid BankAccount account){
  }

  public record PaymentMethodDto(@Valid DirectPaymentMethodDto directMethod,
                                 @Valid Address mailMethod){

  }
}
