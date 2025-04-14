package com.github.PiotrDuma.payroll.domain.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.bank.Bank;
import com.github.PiotrDuma.payroll.common.bankAccount.BankAccount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.EmployeeController.AddEmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.EmployeeController.ClassificationDto;
import com.github.PiotrDuma.payroll.domain.employee.EmployeeController.PutRequestDto;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddCommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddHourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddSalariedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.CommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.DirectPaymentMethodDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.HourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.PaymentMethodDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.SalariedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
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

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

  private static final String URI = "/employees/{id}";
  private static final String URL = "/employees";
  private static final UUID ID = UUID.randomUUID();
  private static final Salary SALARY = new Salary(1234d);
  private static final Address ADDRESS = new Address("address123");
  private static final EmployeeName NAME = new EmployeeName("employeeName123");

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReceiveEmployee receiveEmployee;
  @MockBean
  private ChangeEmployeeService changeService;

  @MockBean
  private AddEmployeeTransactionFactory employeeFactory;
  @InjectMocks
  private EmployeeController employeeController;

  @Captor
  ArgumentCaptor<EmployeeId> idCaptor;

  @Test
  void shouldReturnEmptyListOfEmployees() throws Exception {
    List<EmployeeResponse> list = new ArrayList<>();
    when(this.receiveEmployee.findAll()).thenReturn(list);

    ResultActions result = this.mockMvc.perform(get("/employees"));

    result.andExpect(status().isOk())
        .andExpect(content().string("[]"));
    ;
  }

  @Test
  void shouldReturnListOfEmployees() throws Exception {
    EmployeeResponse employee = mock(EmployeeResponse.class);
    EmployeeDto dto = new EmployeeDto(UUID.randomUUID().toString(), "Name", "address");

    List<EmployeeResponse> list = List.of(employee);
    when(this.receiveEmployee.findAll()).thenReturn(list);
    when(employee.toDto()).thenReturn(dto);

    ResultActions result = this.mockMvc.perform(get(URL));

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", Matchers.containsString(dto.getId())))
        .andExpect(jsonPath("$[0].name", Matchers.containsString(dto.getName())))
        .andExpect(jsonPath("$[0].address", Matchers.containsString(dto.getAddress())));
  }

  @Test
  void getEmployeeShouldThrowWhenIdFormatIsInvalid() throws Exception {
    ResultActions result = this.mockMvc.perform(get("/employees/1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getEmployeeShouldThrowWhenEmployeeIsNotFound() throws Exception {
    String message = "employee not found";
    doThrow(new ResourceNotFoundException(message)).when(this.receiveEmployee).find(any());

    ResultActions result = this.mockMvc.perform(get(URI, ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage", Matchers.containsString(message)));
  }

  @Test
  void shouldReturnEmployeeById() throws Exception {
    EmployeeResponse employee = mock(EmployeeResponse.class);
    EmployeeDto dto = new EmployeeDto(ID.toString(), "Name", "address");

    when(this.receiveEmployee.find(any())).thenReturn(employee);
    when(employee.toDto()).thenReturn(dto);

    ResultActions result = this.mockMvc.perform(get(URI, ID));

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.containsString(dto.getId())))
        .andExpect(jsonPath("$.name", Matchers.containsString(dto.getName())))
        .andExpect(jsonPath("$.address", Matchers.containsString(dto.getAddress())));
  }

  @Test
  void postSalariedEmployeeShouldReturnId() throws Exception {
    AddEmployeeTransaction transaction = mock(AddEmployeeTransaction.class);
    AddEmployeeDto dto = new AddEmployeeDto(getAddSalariedDto(), null, null);

    when(this.employeeFactory.initSalariedEmployeeTransaction(any(), any(), any()))
        .thenReturn(transaction);
    when(transaction.execute()).thenReturn(new EmployeeId(ID));

    ResultActions result = this.mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    verify(this.employeeFactory, times(1)).initSalariedEmployeeTransaction(
        any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(ID.toString())));
  }

  @Test
  void postHourlyEmployeeShouldReturnId() throws Exception {
    AddEmployeeTransaction transaction = mock(AddEmployeeTransaction.class);
    AddEmployeeDto dto = new AddEmployeeDto(null, getAddHourlyDto(), null);

    when(this.employeeFactory.initHourlyEmployeeTransaction(any(), any(), any()))
        .thenReturn(transaction);
    when(transaction.execute()).thenReturn(new EmployeeId(ID));

    ResultActions result = this.mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    verify(this.employeeFactory, times(1)).initHourlyEmployeeTransaction(
        any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(ID.toString())));
  }

  @Test
  void postCommissionedEmployeeShouldReturnId() throws Exception {
    AddEmployeeTransaction transaction = mock(AddEmployeeTransaction.class);
    AddEmployeeDto dto = new AddEmployeeDto(null, null, getAddCommissionedDto());

    when(this.employeeFactory.initCommissionedEmployeeTransaction(any(), any(), any(), any()))
        .thenReturn(transaction);
    when(transaction.execute()).thenReturn(new EmployeeId(ID));

    ResultActions result = this.mockMvc.perform(post(URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    verify(this.employeeFactory, times(1)).initCommissionedEmployeeTransaction(
        any(), any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(ID.toString())));
  }

  @Test
  void putMethodShouldInvokeNameChange() throws Exception {
    PutRequestDto dto = new PutRequestDto(NAME, null, null, null);
    ArgumentCaptor<EmployeeName> nameCaptor = ArgumentCaptor.forClass(EmployeeName.class);

    invokePutMethod(dto);

    verify(this.changeService, times(1)).changeNameTransaction(
        idCaptor.capture(), nameCaptor.capture());

    assertEquals(ID, idCaptor.getValue().getId());
    assertEquals(NAME, nameCaptor.getValue());
  }

  @Test
  void putMethodShouldInvokeAddressChange() throws Exception {
    PutRequestDto dto = new PutRequestDto(null, ADDRESS, null, null);
    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);

    invokePutMethod(dto);

    verify(this.changeService, times(1)).changeAddressTransaction(
        idCaptor.capture(), addressCaptor.capture());

    assertEquals(ID, idCaptor.getValue().getId());
    assertEquals(ADDRESS, addressCaptor.getValue());
  }

  @Test
  void putMethodShouldInvokeNoChanges() throws Exception {
    PutRequestDto dto = new PutRequestDto(null, null, null, null);

    invokePutMethod(dto);

    verify(this.changeService, times(0)).changeNameTransaction(any(), any());
    verify(this.changeService, times(0)).changeAddressTransaction(any(), any());
    verify(this.changeService, times(0)).changeSalariedClassificationTransaction(any(), any());
    verify(this.changeService, times(0)).changeHourlyClassificationTransaction(any(), any());
    verify(this.changeService, times(0)).changeCommissionedClassificationTransaction(any(), any(),
        any());
    verify(this.changeService, times(0)).changeHoldPaymentMethodTransaction(any());
    verify(this.changeService, times(0)).changeDirectPaymentMethodTransaction(any(), any(), any());
    verify(this.changeService, times(0)).changeMailPaymentMethodTransaction(any(), any());
  }

  @Test
  void putMethodShouldInvokeSalariedClassificationAndDirectPaymentChange() throws Exception {
    Bank bank = new Bank("bankname");
    BankAccount account = new BankAccount("01234567890123456789012345");

    PutRequestDto dto = new PutRequestDto(null, null,
        new ClassificationDto(new SalariedDto(SALARY), null, null),
        new PaymentMethodDto(new DirectPaymentMethodDto(bank, account), null));

    ArgumentCaptor<Salary> salaryCaptor = ArgumentCaptor.forClass(Salary.class);
    ArgumentCaptor<Bank> bankCaptor = ArgumentCaptor.forClass(Bank.class);
    ArgumentCaptor<BankAccount> accountCaptor = ArgumentCaptor.forClass(BankAccount.class);

    invokePutMethod(dto);

    verify(this.changeService, times(1))
        .changeSalariedClassificationTransaction(idCaptor.capture(), salaryCaptor.capture());

    assertEquals(ID, idCaptor.getValue().getId());
    assertEquals(SALARY, salaryCaptor.getValue());

    verify(this.changeService, times(1))
        .changeDirectPaymentMethodTransaction(idCaptor.capture(), bankCaptor.capture(), accountCaptor.capture());

    assertEquals(ID, idCaptor.getValue().getId());
    assertEquals(bank, bankCaptor.getValue());
    assertEquals(account, accountCaptor.getValue());
  }

  @Test
  void putMethodShouldInvokeMailPaymentAndHourlyClassificationChange() throws Exception {
    HourlyRate rate = new HourlyRate(12.5d);
    PutRequestDto dto = new PutRequestDto(null, null,
        new ClassificationDto(null, new HourlyDto(rate), null),
        new PaymentMethodDto(null, ADDRESS));
    ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
    ArgumentCaptor<HourlyRate> rateCaptor = ArgumentCaptor.forClass(HourlyRate.class);

    invokePutMethod(dto);

    verify(this.changeService, times(1))
        .changeHourlyClassificationTransaction(idCaptor.capture(), rateCaptor.capture());

    assertEquals(ID, idCaptor.getValue().getId());
    assertEquals(rate, rateCaptor.getValue());

    verify(this.changeService, times(1))
        .changeMailPaymentMethodTransaction(idCaptor.capture(), addressCaptor.capture());

    assertEquals(ID, idCaptor.getValue().getId());
    assertEquals(ADDRESS, addressCaptor.getValue());
  }

  @Test
  void putMethodShouldInvokeHoldPaymentAndCommissionedClassificationChange() throws Exception {
    CommissionRate rate = new CommissionRate(12.5d);
    PutRequestDto dto = new PutRequestDto(null, null,
        new ClassificationDto(null, null, new CommissionedDto(SALARY, rate)),
        new PaymentMethodDto(null, null));
    ArgumentCaptor<CommissionRate> rateCaptor = ArgumentCaptor.forClass(CommissionRate.class);
    ArgumentCaptor<Salary> salaryCaptor = ArgumentCaptor.forClass(Salary.class);

    invokePutMethod(dto);

    verify(this.changeService, times(1))
        .changeCommissionedClassificationTransaction(idCaptor.capture(),
            salaryCaptor.capture(), rateCaptor.capture());

    assertEquals(ID, idCaptor.getValue().getId());
    assertEquals(SALARY, salaryCaptor.getValue());
    assertEquals(rate, rateCaptor.getValue());

    verify(this.changeService, times(1))
        .changeHoldPaymentMethodTransaction(idCaptor.capture());

    assertEquals(ID, idCaptor.getValue().getId());
  }

  private ResultActions invokePutMethod(PutRequestDto dto) throws Exception {
    return this.mockMvc.perform(put(URI, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapperProvider.createJson().writeValueAsString(dto)))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  private AddCommissionedDto getAddCommissionedDto() {
    return new AddCommissionedDto(
        NAME,
        ADDRESS,
        new CommissionedDto(
           SALARY,
            new CommissionRate(12.5d)
        )
    );
  }

  private AddHourlyDto getAddHourlyDto() {
    return new AddHourlyDto(
        NAME,
        ADDRESS,
        new HourlyDto(new HourlyRate(12.5d)
        )
    );
  }

  private AddSalariedDto getAddSalariedDto() {
    return new AddSalariedDto(
        NAME,
        ADDRESS,
        new SalariedDto(SALARY)
    );
  }
}