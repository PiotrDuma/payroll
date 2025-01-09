package com.github.PiotrDuma.payroll.domain.employee;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReceiveEmployee receiveEmployee;

  @InjectMocks
  private EmployeeController employeeController;

  @Test
  void shouldReturnEmptyListOfEmployees() throws Exception{
    List<EmployeeResponse> list = new ArrayList<>();
    when(this.receiveEmployee.findAll()).thenReturn(list);

    ResultActions result = this.mockMvc.perform(get("/employees"));

    result.andExpect(status().isOk())
        .andExpect(content().string("[]"));;
  }

  @Test
  void shouldReturnListOfEmployees() throws Exception{
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
  void getEmployeeShouldThrowWhenIdFormatIsInvalid() throws Exception{
    ResultActions result = this.mockMvc.perform(get("/employees/1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getEmployeeShouldThrowWhenEmployeeIsNotFound() throws Exception{
    UUID id = UUID.randomUUID();
    String message = "employee not found";
    doThrow(new ResourceNotFoundException(message)).when(this.receiveEmployee).find(any());

    ResultActions result = this.mockMvc.perform(get("/employees/"+id.toString()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage", Matchers.containsString(message)));
  }

  @Test
  void shouldReturnEmployeeById() throws Exception{
    EmployeeResponse employee = mock(EmployeeResponse.class);
    UUID id = UUID.randomUUID();
    EmployeeDto dto = new EmployeeDto(id.toString(), "Name", "address");

    when(this.receiveEmployee.find(any())).thenReturn(employee);
    when(employee.toDto()).thenReturn(dto);

    ResultActions result = this.mockMvc.perform(get("/employees/"+id));

    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.containsString(dto.getId())))
        .andExpect(jsonPath("$.name", Matchers.containsString(dto.getName())))
        .andExpect(jsonPath("$.address", Matchers.containsString(dto.getAddress())));
  }
}