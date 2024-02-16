package com.github.PiotrDuma.payroll.domain.payment.method.api;

import com.github.PiotrDuma.payroll.common.salary.Salary;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentDto(UUID id, LocalDate date, Salary salary) {

}
