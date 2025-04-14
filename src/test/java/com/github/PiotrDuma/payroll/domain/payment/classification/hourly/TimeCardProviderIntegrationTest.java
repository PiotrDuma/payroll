package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.model.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Tag("IntegrationTest")
@ActiveProfiles("test")
class TimeCardProviderIntegrationTest {

  @Autowired
  private TimeCardProvider timeCardProvider;

  @Autowired
  private ReceiveEmployee employeeRepo;

  @Autowired
  private AddEmployeeTransactionFactory addEmployeeFactory;

  @Test
  void addTimeCardShouldSaveEmployeeTimecard(){
    EmployeeId id = initEmployee();
    LocalDate date = LocalDate.of(2000, 1, 1);
    Hours hours = new Hours(12.5);
    this.timeCardProvider.addOrUpdateTimeCard(id, date, hours);

    HourlyClassificationEntity classification =
        (HourlyClassificationEntity) this.employeeRepo.find(id).getPaymentClassification();
    TimeCard timecard = classification.getTimeCards().iterator().next();

    assertEquals(1, classification.getTimeCards().size());

    assertEquals(id, timecard.getEmployeeId());
    assertEquals(date, timecard.getDate());
    assertEquals(hours, timecard.getHours());
  }

  @Test
  void addTimeCardShouldUpdateEmployeeTimecardWhenGivenTimecardExists(){
    EmployeeId id = initEmployee();
    LocalDate date = LocalDate.of(2000, 1, 1);
    Hours hours = new Hours(12.5);
    Hours newHours = new Hours(5.5);

    this.timeCardProvider.addOrUpdateTimeCard(id, date, hours);

    HourlyClassificationEntity classification =
        (HourlyClassificationEntity) this.employeeRepo.find(id).getPaymentClassification();
    assertEquals(1, classification.getTimeCards().size());

    this.timeCardProvider.addOrUpdateTimeCard(id, date, newHours);

    HourlyClassificationEntity classification2 =
        (HourlyClassificationEntity) this.employeeRepo.find(id).getPaymentClassification();
    TimeCard updated = classification2.getTimeCards().iterator().next();

    assertEquals(id, updated.getEmployeeId());
    assertEquals(date, updated.getDate());
    assertEquals(newHours, updated.getHours());
  }

  @Test
  void addTimeCardShouldThrowResourceNotFoundExceptionWhenEmployeeNotFound(){
    EmployeeId id = new EmployeeId(UUID.randomUUID());
    LocalDate date = LocalDate.of(2000, 1, 1);
    Hours hours = new Hours(12.5);

    assertThrows(ResourceNotFoundException.class, () ->
        this.timeCardProvider.addOrUpdateTimeCard(id, date, hours));
  }

  private EmployeeId initEmployee(){
    Address address = new Address("ADDRESS");
    EmployeeName name = new EmployeeName("name");
    HourlyRate rate = new HourlyRate(12);
    return this.addEmployeeFactory.initHourlyEmployeeTransaction(address, name, rate)
        .execute();
  }
}