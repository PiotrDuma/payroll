package com.github.PiotrDuma.payroll.domain.employee;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.EmployeeController.AddEmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddCommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddHourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.AddSalariedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.CommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.HourlyDto;
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
  private static final UUID ID = UUID.randomUUID();

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReceiveEmployee receiveEmployee;

  @MockBean
  private AddEmployeeTransactionFactory employeeFactory;
  @InjectMocks
  private EmployeeController employeeController;

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

    ResultActions result = this.mockMvc.perform(get("/employees"));

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
    UUID id = UUID.randomUUID();
    String message = "employee not found";
    doThrow(new ResourceNotFoundException(message)).when(this.receiveEmployee).find(any());

    ResultActions result = this.mockMvc.perform(get("/employees/" + id.toString()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage", Matchers.containsString(message)));
  }

  @Test
  void shouldReturnEmployeeById() throws Exception {
    EmployeeResponse employee = mock(EmployeeResponse.class);
    UUID id = UUID.randomUUID();
    EmployeeDto dto = new EmployeeDto(id.toString(), "Name", "address");

    when(this.receiveEmployee.find(any())).thenReturn(employee);
    when(employee.toDto()).thenReturn(dto);

    ResultActions result = this.mockMvc.perform(get("/employees/" + id));

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

    ResultActions result = this.mockMvc.perform(post("/employees")
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

    ResultActions result = this.mockMvc.perform(post("/employees")
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

    ResultActions result = this.mockMvc.perform(post("/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    verify(this.employeeFactory, times(1)).initCommissionedEmployeeTransaction(
        any(), any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(ID.toString())));
  }

  private AddCommissionedDto getAddCommissionedDto(){
    return new AddCommissionedDto(
        new EmployeeName("name"),
        new Address("address"),
        new CommissionedDto(
            new Salary(1234d),
            new CommissionRate(12.5d)
        )
    );
  }

  private AddHourlyDto getAddHourlyDto(){
    return new AddHourlyDto(
        new EmployeeName("name"),
        new Address("address"),
        new HourlyDto(new HourlyRate(12.5d)
        )
    );
  }

  private AddSalariedDto getAddSalariedDto() {
    return new AddSalariedDto(
        new EmployeeName("name"),
        new Address("address"),
        new SalariedDto(
            new Salary(1234d)
        )
    );
  }
}