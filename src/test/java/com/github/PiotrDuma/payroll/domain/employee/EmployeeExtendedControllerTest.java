package com.github.PiotrDuma.payroll.domain.employee;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.salary.Salary;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransaction;
import com.github.PiotrDuma.payroll.domain.employee.api.AddEmployeeTransactionFactory;
import com.github.PiotrDuma.payroll.domain.employee.api.ChangeEmployeeService;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.CommissionedDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.HourlyDto;
import com.github.PiotrDuma.payroll.domain.employee.api.model.EmployeeRequestDto.SalariedDto;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.CommissionRate;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.HourlyRate;
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
  private static final UUID ID = UUID.randomUUID();
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
  void postHourlyEmployeeShouldReturnId() throws Exception {
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
  void postCommissionedEmployeeShouldReturnId() throws Exception {
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