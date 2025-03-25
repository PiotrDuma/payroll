package com.github.PiotrDuma.payroll.domain.employee;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeRequestDto.CommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeRequestDto.HourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeRequestDto.SalariedDto;
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

@WebMvcTest(controllers = EmployeeExtendedController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class EmployeeExtendedControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AddEmployeeTransactionFactory addEmployee;

  @MockBean
  private ChangeEmployeeService changeEmployeeService;

  @InjectMocks
  private EmployeeExtendedController controller;

  @Test
  void postSalariedEmployeeShouldReturnId() throws Exception {
    String endpoint = "/employees/salaried";
    UUID id = UUID.randomUUID();
    String url = "/employees/" + id;

    AddEmployeeTransaction transaction = mock(AddEmployeeTransaction.class);
    SalariedDto dto = new SalariedDto("address", "name", 1234d);

    when(this.addEmployee.initSalariedEmployeeTransaction(any(), any(), any()))
        .thenReturn(transaction);
    when(transaction.execute()).thenReturn(new EmployeeId(id));

    ResultActions result = this.mockMvc.perform(post(endpoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    verify(this.addEmployee, times(1)).initSalariedEmployeeTransaction(
        any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(id.toString())))
        .andExpect(MockMvcResultMatchers.header().stringValues("Location", url));
  }

  @Test
  void postHourlyEmployeeShouldReturnId() throws Exception {
    String endpoint = "/employees/hourly";
    UUID id = UUID.randomUUID();
    String url = "/employees/" + id;

    AddEmployeeTransaction transaction = mock(AddEmployeeTransaction.class);
    HourlyDto dto = new HourlyDto("address", "name", 12.5d);

    when(this.addEmployee.initHourlyEmployeeTransaction(any(), any(), any()))
        .thenReturn(transaction);
    when(transaction.execute()).thenReturn(new EmployeeId(id));

    ResultActions result = this.mockMvc.perform(post(endpoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    verify(this.addEmployee, times(1)).initHourlyEmployeeTransaction(
        any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(id.toString())))
        .andExpect(MockMvcResultMatchers.header().stringValues("Location", url));
  }

  @Test
  void postCommissionedEmployeeShouldReturnId() throws Exception {
    String endpoint = "/employees/commissioned";
    UUID id = UUID.randomUUID();
    String url = "/employees/" + id;

    AddEmployeeTransaction transaction = mock(AddEmployeeTransaction.class);
    CommissionedDto dto = new CommissionedDto("address", "name", 500d, 12d);

    when(this.addEmployee.initCommissionedEmployeeTransaction(any(), any(), any(), any()))
        .thenReturn(transaction);
    when(transaction.execute()).thenReturn(new EmployeeId(id));

    ResultActions result = this.mockMvc.perform(post(endpoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    verify(this.addEmployee, times(1)).initCommissionedEmployeeTransaction(
        any(), any(), any(), any());

    result.andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.containsString(id.toString())))
        .andExpect(MockMvcResultMatchers.header().stringValues("Location", url));
  }
}