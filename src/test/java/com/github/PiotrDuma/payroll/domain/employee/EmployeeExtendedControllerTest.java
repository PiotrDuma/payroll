package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.CommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.DirectPaymentMethodDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.HourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.PaymentMethodDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.SalariedDto;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = EmployeeExtendedController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class EmployeeExtendedControllerTest {
  private static final UUID ID = UUID.randomUUID();
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AddEmployeeTransactionFactory addEmployee;

  @MockBean
  private ChangeEmployeeService changeEmployeeService;

  @InjectMocks
  private EmployeeExtendedController controller;

  @Captor
  ArgumentCaptor<EmployeeId> idCaptor;

  @Test
  void postSalariedEmployeeShouldExecuteService() throws Exception {
    String endpoint = "/employees/salaried";
    String uri = "/employees/" + ID;

    AddEmployeeTransaction transaction = mock(AddEmployeeTransaction.class);

    when(this.addEmployee.initSalariedEmployeeTransaction(any(), any(), any()))
        .thenReturn(transaction);
    when(transaction.execute()).thenReturn(new EmployeeId(ID));

    ResultActions result = this.mockMvc.perform(post(endpoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(getSalariedDto())));

    verify(this.addEmployee, times(1)).initSalariedEmployeeTransaction(
        any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(ID.toString())))
        .andExpect(MockMvcResultMatchers.header().stringValues("Location", uri));
  }

  @Test
  void putSalariedEmployeeShouldInvokeService() throws Exception {
    String uri = "/employees/{id}/salaried";
    ArgumentCaptor<Salary> salaryCaptor = ArgumentCaptor.forClass(Salary.class);

    ChangeEmployeeTransaction transaction = mock(ChangeSalariedClassificationTransaction.class);

    ResultActions result = this.mockMvc.perform(put(uri, ID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(getSalariedDto())))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(this.changeEmployeeService, times(1))
        .changeSalariedClassificationTransaction(idCaptor.capture(), salaryCaptor.capture());

    assertEquals(idCaptor.getValue().getId().toString(), ID.toString());
    assertEquals(salaryCaptor.getValue(), getSalariedDto().salary());
  }

  @Test
  void postHourlyEmployeeShouldExecuteService() throws Exception {
    String endpoint = "/employees/hourly";
    String uri = "/employees/" + ID;

    AddEmployeeTransaction transaction = mock(AddEmployeeTransaction.class);

    when(this.addEmployee.initHourlyEmployeeTransaction(any(), any(), any()))
        .thenReturn(transaction);
    when(transaction.execute()).thenReturn(new EmployeeId(ID));

    ResultActions result = this.mockMvc.perform(post(endpoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(getHourlyDto())));

    verify(this.addEmployee, times(1)).initHourlyEmployeeTransaction(
        any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(ID.toString())))
        .andExpect(MockMvcResultMatchers.header().stringValues("Location", uri));
  }

  @Test
  void putHourlyEmployeeShouldInvokeService() throws Exception {
    String uri = "/employees/{id}/hourly";
    ArgumentCaptor<HourlyRate> rateCaptor = ArgumentCaptor.forClass(HourlyRate.class);

    ChangeEmployeeTransaction transaction = mock(ChangeHourlyClassificationTransaction.class);

    ResultActions result = this.mockMvc.perform(put(uri, ID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(getHourlyDto())))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(this.changeEmployeeService, times(1))
        .changeHourlyClassificationTransaction(idCaptor.capture(), rateCaptor.capture());

    assertEquals(idCaptor.getValue().getId().toString(), ID.toString());
    assertEquals(rateCaptor.getValue(), getHourlyDto().hourlyRate());
  }

  @Test
  void postCommissionedEmployeeShouldExecuteService() throws Exception {
    String endpoint = "/employees/commissioned";
    String uri = "/employees/" + ID;

    AddEmployeeTransaction transaction = mock(AddEmployeeTransaction.class);

    when(this.addEmployee.initCommissionedEmployeeTransaction(any(), any(), any(), any()))
        .thenReturn(transaction);
    when(transaction.execute()).thenReturn(new EmployeeId(ID));

    ResultActions result = this.mockMvc.perform(post(endpoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(getCommissionedDto())));

    verify(this.addEmployee, times(1)).initCommissionedEmployeeTransaction(
        any(), any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(ID.toString())))
        .andExpect(MockMvcResultMatchers.header().stringValues("Location", uri));
  }

  @Test
  void putCommissionedEmployeeShouldInvokeService() throws Exception {
    String uri = "/employees/{id}/commissioned";
    ArgumentCaptor<Salary> salaryCaptor = ArgumentCaptor.forClass(Salary.class);
    ArgumentCaptor<CommissionRate> rateCaptor = ArgumentCaptor.forClass(CommissionRate.class);

    ChangeEmployeeTransaction transaction = mock(ChangeCommissionedClassificationTransaction.class);

    ResultActions result = this.mockMvc.perform(put(uri, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapperProvider.createJson().writeValueAsString(getCommissionedDto())))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(this.changeEmployeeService, times(1))
        .changeCommissionedClassificationTransaction(idCaptor.capture(),
            salaryCaptor.capture(), rateCaptor.capture());

    assertEquals(idCaptor.getValue().getId().toString(), ID.toString());
    assertEquals(salaryCaptor.getValue(), getCommissionedDto().salary());
    assertEquals(rateCaptor.getValue(), getCommissionedDto().commissionedRate());
  }

  @Test
  void putDirectPaymentMethodShouldInvokeService() throws Exception {
    Bank bank = new Bank("bankname");
    BankAccount account = new BankAccount("01234567890123456789012345");
    String uri = "/employees/{id}/method";
    PaymentMethodDto dto = new PaymentMethodDto(new DirectPaymentMethodDto(bank, account), null);


    ArgumentCaptor<Bank> bankCaptor = ArgumentCaptor.forClass(Bank.class);
    ArgumentCaptor<BankAccount> accountCaptor = ArgumentCaptor.forClass(BankAccount.class);

    ChangeEmployeeTransaction transaction = mock(ChangeDirectMethodTransaction.class);

    ResultActions result = this.mockMvc.perform(put(uri, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapperProvider.createJson().writeValueAsString(dto)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(this.changeEmployeeService, times(1))
        .changeDirectPaymentMethodTransaction(idCaptor.capture(),
            bankCaptor.capture(), accountCaptor.capture());

    assertEquals(idCaptor.getValue().getId().toString(), ID.toString());
    assertEquals(bankCaptor.getValue(), bank);
    assertEquals(accountCaptor.getValue(), account);
  }

  private CommissionedDto getCommissionedDto(){
    return new CommissionedDto(new Address("address"),
        new EmployeeName("name"),
        new Salary(500d),
        new CommissionRate(12d));
  }

  private HourlyDto getHourlyDto(){
    return new HourlyDto(new Address("address"),
        new EmployeeName("name"),
        new HourlyRate(12.5d));
  }

  private SalariedDto getSalariedDto() {
    return new SalariedDto(new Address("address"),
        new EmployeeName("name"),
        new Salary(1234d));
  }
}