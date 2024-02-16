package com.github.PiotrDuma.payroll.domain.employee.api;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;

public interface EmployeeResponse {
  EmployeeId getId();
  EmployeeName getName();
  Address getAddress();
  PaymentSchedule getSchedule();
  PaymentClassification getPaymentClassification();
  PaymentMethod getPaymentMethod();
  //TODO: toDto method
}
