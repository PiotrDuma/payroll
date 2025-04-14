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
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddCommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddHourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddSalariedDto;
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
        .content(ObjectMapperProvider.createJson().writeValueAsString(getAddSalariedDto())));

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

    assertEquals(ID.toString(), idCaptor.getValue().getId().toString());
    assertEquals(getSalariedDto().salary(), salaryCaptor.getValue());
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
        .content(ObjectMapperProvider.createJson().writeValueAsString(getAddHourlyDto())));

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

    assertEquals(ID.toString(), idCaptor.getValue().getId().toString());
    assertEquals(getHourlyDto().hourlyRate(), rateCaptor.getValue());
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
        .content(ObjectMapperProvider.createJson().writeValueAsString(getAddCommissionedDto())));

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

    assertEquals(ID.toString(), idCaptor.getValue().getId().toString());
    assertEquals(getCommissionedDto().salary(), salaryCaptor.getValue());
    assertEquals(getCommissionedDto().commissionedRate(), rateCaptor.getValue());
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

    assertEquals(ID.toString(), idCaptor.getValue().getId().toString());
    assertEquals(bank, bankCaptor.getValue());
    assertEquals(account, accountCaptor.getValue());
  }

  @Test
  void putMailPaymentMethodShouldInvokeService() throws Exception {
    String uri = "/employees/{id}/method";
    Address address = new Address("address");
    PaymentMethodDto dto = new PaymentMethodDto(null, address);
    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);

    ChangeEmployeeTransaction transaction = mock(ChangeMailMethodTransaction.class);

    ResultActions result = this.mockMvc.perform(put(uri, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapperProvider.createJson().writeValueAsString(dto)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(this.changeEmployeeService, times(1))
        .changeMailPaymentMethodTransaction(idCaptor.capture(), addressCaptor.capture());

    assertEquals(ID.toString(), idCaptor.getValue().getId().toString());
    assertEquals(address, addressCaptor.getValue());
  }

  @Test
  void putHoldPaymentMethodShouldInvokeService() throws Exception {
    String uri = "/employees/{id}/method";
    ChangeEmployeeTransaction transaction = mock(ChangeHoldMethodTransaction.class);
    PaymentMethodDto dto = new PaymentMethodDto(null, null);

    ResultActions result = this.mockMvc.perform(put(uri, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapperProvider.createJson().writeValueAsString(dto)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(this.changeEmployeeService, times(1))
        .changeHoldPaymentMethodTransaction(idCaptor.capture());

    assertEquals(ID.toString(), idCaptor.getValue().getId().toString());
  }

  @Test
  void putAddressShouldInvokeChangeAddressService() throws Exception {
    String uri = "/employees/{id}/address";
    Address address = new Address("address");
    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);

    ChangeEmployeeTransaction transaction = mock(ChangeAddressTransaction.class);

    ResultActions result = this.mockMvc.perform(put(uri, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapperProvider.createJson().writeValueAsString(address)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(this.changeEmployeeService, times(1))
        .changeAddressTransaction(idCaptor.capture(), addressCaptor.capture());

    assertEquals(ID.toString(), idCaptor.getValue().getId().toString());
    assertEquals(address, addressCaptor.getValue());
  }

  @Test
  void putNameShouldInvokeChangeNameService() throws Exception {
    String uri = "/employees/{id}/name";
    EmployeeName name = new EmployeeName("name");
    ArgumentCaptor<EmployeeName> nameCaptor = ArgumentCaptor.forClass(EmployeeName.class);

    ChangeEmployeeTransaction transaction = mock(ChangeNameTransaction.class);

    ResultActions result = this.mockMvc.perform(put(uri, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapperProvider.createJson().writeValueAsString(name)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    verify(this.changeEmployeeService, times(1))
        .changeNameTransaction(idCaptor.capture(), nameCaptor.capture());

    assertEquals(ID.toString(), idCaptor.getValue().getId().toString());
    assertEquals(name, nameCaptor.getValue());
  }

  private AddCommissionedDto getAddCommissionedDto(){
    return new AddCommissionedDto(
        new EmployeeName("name"),
        new Address("address"),
        getCommissionedDto());
  }

  private CommissionedDto getCommissionedDto(){
    return new CommissionedDto(new Salary(1234d), new CommissionRate(12.5d));
  }

  private AddHourlyDto getAddHourlyDto(){
    return new AddHourlyDto(
        new EmployeeName("name"),
        new Address("address"),
        getHourlyDto()
    );
  }

  private HourlyDto getHourlyDto(){
    return new HourlyDto(new HourlyRate(12.5d));
  }

  private AddSalariedDto getAddSalariedDto() {
    return new AddSalariedDto(
        new EmployeeName("name"),
        new Address("address"),
        getSalariedDto());
  }

  private SalariedDto getSalariedDto(){
    return new SalariedDto(new Salary(1234d));
  }
}