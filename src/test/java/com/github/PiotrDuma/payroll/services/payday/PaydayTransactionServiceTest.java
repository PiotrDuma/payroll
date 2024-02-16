package com.github.PiotrDuma.payroll.services.payday;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
import com.github.PiotrDuma.payroll.services.payday.api.PaydayTransaction;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaydayTransactionServiceTest {
  private static final ZonedDateTime NOW = ZonedDateTime.of(2000,1,
      1, 1, 1, 12, 0, ZoneId.of("UTC"));
  private static final Salary EMPLOYEE_SALARY = new Salary(1234);
  private static final Amount EMPLOYEE_UNION_DUES = new Amount(234);

  @Mock
  private ReceiveEmployee receiveEmployee;
  @Mock
  private UnionAffiliationService unionService;
  @Mock
  private Clock clock;
  @Mock
  private PaycheckRepository paycheckRepository;
  private PaydayTransaction service;

  @BeforeEach
  void setUp() {
    when(this.clock.instant()).thenReturn(NOW.toInstant());
    when(this.clock.getZone()).thenReturn(NOW.getZone());

    this.service = new PaydayTransactionService(receiveEmployee, unionService, clock, paycheckRepository);
  }

  @Test
  void executeShouldNotSaveAnyPaychecksWhenEmployeePaydayListIsEmpty() {
    when(this.receiveEmployee.findAll()).thenReturn(List.of());

    this.service.execute();

    verify(this.paycheckRepository, times(0)).save(any());
  }

  @Test
  void executeShouldNotSaveAnyPaychecksWhenNoEmployeeHasPayday() {
    EmployeeResponse employee = mock(EmployeeResponse.class);
    PaymentSchedule schedule = mock(PaymentSchedule.class);

    when(employee.getSchedule()).thenReturn(schedule);
    when(schedule.isPayday(any())).thenReturn(false);
    when(this.receiveEmployee.findAll()).thenReturn(List.of(employee));

    this.service.execute();

    verify(this.paycheckRepository, times(0)).save(any());
  }

  @Test
  void executeShouldInvokePaymentMethodAndSavePaycheckWithValidValues(){
    EmployeeResponse employee = mockEmployeeWithPayday();
    when(this.receiveEmployee.findAll()).thenReturn(List.of(employee));

    this.service.execute();
    ArgumentCaptor<PaycheckEntity> captor = ArgumentCaptor.forClass(PaycheckEntity.class);

    verify(employee.getPaymentMethod(), times(1)).executePayment(any(), any());
    verify(this.paycheckRepository, times(1)).save(captor.capture());

    PaycheckEntity result = captor.getValue();
    assertEquals(EMPLOYEE_SALARY.getSalary(), result.getSalary().getSalary());
    assertEquals(EMPLOYEE_UNION_DUES.getAmount(), result.getUnionDues().getAmount());
    assertEquals(new BigDecimal(1000), result.getNetSalary().getSalary());
  }

  @Test
  void executeShouldInvokeOnlyForEmployeesWithPayday(){
    EmployeeResponse employee1 = mockEmployeeWithPayday();
    EmployeeResponse employee2 = mock(EmployeeResponse.class);
    PaymentSchedule schedule2 = mock(PaymentSchedule.class);
    when(schedule2.isPayday(any())).thenReturn(false);
    when(employee2.getSchedule()).thenReturn(schedule2);

    when(this.receiveEmployee.findAll()).thenReturn(List.of(employee1, employee2));

    this.service.execute();

    verify(employee1.getPaymentMethod(), times(1)).executePayment(any(), any());
    verify(this.paycheckRepository, times(1)).save(any());
  }

  @Test
  void executeShouldInvokeForAllEmployeesWithPayday(){
    EmployeeResponse employee2 = mock(EmployeeResponse.class);
    PaymentSchedule schedule2 = mock(PaymentSchedule.class);
    when(schedule2.isPayday(any())).thenReturn(false);
    when(employee2.getSchedule()).thenReturn(schedule2);

    EmployeeResponse employee1 = mockEmployeeWithPayday();
    EmployeeResponse employee3 = mockEmployeeWithPayday();
    EmployeeResponse employee4 = mockEmployeeWithPayday();

    when(this.receiveEmployee.findAll()).thenReturn(List.of(employee1, employee2, employee3, employee4));

    this.service.execute();

    verify(employee1.getPaymentMethod(), times(1)).executePayment(any(), any());
    verify(employee3.getPaymentMethod(), times(1)).executePayment(any(), any());
    verify(employee4.getPaymentMethod(), times(1)).executePayment(any(), any());
    verify(this.paycheckRepository, times(3)).save(any());
  }

  private EmployeeResponse mockEmployeeWithPayday() {
    EmployeeResponse employee = mock(EmployeeResponse.class);

    PaymentSchedule schedule = mock(PaymentSchedule.class);
    PaymentClassification classification = mock(PaymentClassification.class);
    PaymentMethod paymentMethod = mock(PaymentMethod.class);

    when(employee.getId()).thenReturn(new EmployeeId(UUID.randomUUID()));
    when(employee.getSchedule()).thenReturn(schedule);
    when(employee.getPaymentClassification()).thenReturn(classification);
    when(employee.getPaymentMethod()).thenReturn(paymentMethod);

    when(schedule.isPayday(any())).thenReturn(true);
    when(classification.calculatePay(any())).thenReturn(EMPLOYEE_SALARY);
    when(this.unionService.countMembershipCharges(any(), any())).thenReturn(EMPLOYEE_UNION_DUES);
    return employee;
  }
}