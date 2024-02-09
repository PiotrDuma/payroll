package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionedClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.salary.api.SalariedClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethodFactory;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentScheduleFactory;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ChangeEmployeeTransactionFactoryTest {
  private static final EmployeeId EMPLOYEE_ID = new EmployeeId(UUID.randomUUID());

  @Mock private EmployeeRepository repo;
  @Mock private PaymentMethodFactory methodFactory;
  @Mock private PaymentScheduleFactory scheduleFactory;
  @Mock private SalariedClassification salariedClassification;
  @Mock private HourlyClassification hourlyClassification;
  @Mock private CommissionedClassification commissionedClassification;

  private ChangeEmployeeTransactionFactory factory;

  @BeforeEach
  void setUp(){
    this.factory = new ChangeEmployeeTransactionFactory(repo, methodFactory, scheduleFactory,
        salariedClassification, hourlyClassification, commissionedClassification);
  }

  @Test
  void shouldReturnChangeNameTransaction(){
    EmployeeName name = new EmployeeName("NAME");
    int transactionCode = ChangeEmployeeTransaction.NAME;

    ChangeEmployeeTransaction result = this.factory.create(transactionCode, EMPLOYEE_ID, name);

    assertTrue(result instanceof ChangeNameTransaction);
    assertEquals(name, ((ChangeNameTransaction) result).getName());
    assertEquals(EMPLOYEE_ID, ((ChangeNameTransaction) result).getId());
  }
}