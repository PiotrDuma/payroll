package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyClassification;
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

      default -> null;
    };
  }
}
