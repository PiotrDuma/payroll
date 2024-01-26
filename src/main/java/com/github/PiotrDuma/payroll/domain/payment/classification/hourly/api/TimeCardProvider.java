package com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api;

import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeId;
import java.time.LocalDate;

public interface TimeCardProvider {
  void addOrUpdateTimeCard(EmployeeId employeeId, LocalDate date, Hours hours);
}