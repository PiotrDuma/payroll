package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
class TimeCardProviderService implements TimeCardProvider {
  private static final String MESSAGE = "Employee %s cannot record time cards";
  private final ReceiveEmployee employeeRepo;

  @Autowired
  public TimeCardProviderService(ReceiveEmployee employeeRepo) {
    this.employeeRepo = employeeRepo;
  }

  @Override
  public void addOrUpdateTimeCard(EmployeeId employeeId, LocalDate date, Hours hours) {
    HourlyClassificationEntity employeeClassification = getHourlyClassification(employeeId);
    employeeClassification.addOrUpdateTimeCard(employeeId, date, hours);
  }

  private HourlyClassificationEntity getHourlyClassification(EmployeeId employeeId){
    PaymentClassification paymentClassification = this.employeeRepo.find(employeeId)
        .getPaymentClassification();
    try{
      return (HourlyClassificationEntity) paymentClassification;
    }catch (Exception e){
      throw new InvalidArgumentException(String.format(MESSAGE, employeeId));
    }
  }
}
