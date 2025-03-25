package com.github.PiotrDuma.payroll.domain.employee.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class EmployeeRequestDto {

  public record SalariedDto(@NotNull(message = "Address cannot empty") String address,
                            @NotNull(message = "Name cannot empty") String name,
                            @NotNull(message = "Salary cannot be empty")
                            @DecimalMin(value = "0.0", message = "Salary cannot be lower than 0") Double salary){
    public SalariedDto(String address, String name,  Double salary) {
      this.address = address;
      this.name = name;
      this.salary = salary;
    }
  }
  public record HourlyDto(@NotNull(message = "Address cannot empty") String address,
                          @NotNull(message = "Name cannot empty") String name,
                          @NotNull(message = "Hourly rate cannot be empty")
                          @DecimalMin(value = "0.0", message = "Hourly rate cannot be lower than 0") Double hourlyRate) {
    public HourlyDto(String address, String name, Double hourlyRate) {
      this.address = address;
      this.name = name;
      this.hourlyRate = hourlyRate;
    }
  }
  public record CommissionedDto(@NotNull(message = "Address cannot empty") String address,
                                @NotNull(message = "Name cannot empty") String name,
                                @NotNull(message = "Salary cannot be empty")
                                @DecimalMin(value = "0.0", message = "Salary cannot be lower than 0") Double salary,
                                @NotNull(message = "Commissioned rate cannot be empty")
                                @DecimalMin(value = "0.0", message = "Commissioned rate cannot be lower than 0") Double commissionedRate) {
    public CommissionedDto(String address, String name, Double salary, Double commissionedRate) {
      this.address = address;
      this.name = name;
      this.salary = salary;
      this.commissionedRate = commissionedRate;
    }
  }
}
