package com.github.PiotrDuma.payroll.domain.payment.classification.hourly;

import static com.github.PiotrDuma.payroll.domain.payment.classification.hourly.TimeCardController.TimeCardRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.Hours;
import com.github.PiotrDuma.payroll.domain.payment.classification.hourly.api.TimeCardProvider;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TimeCardController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class TimeCardControllerTest {
  private static final String ID = UUID.randomUUID().toString();
  private static final String URL = "/employees/{id}/timecard";
  @MockBean
  private TimeCardProvider service;

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MockMvc mockMvc;

  @InjectMocks
  private TimeCardController controller;

  @Captor
  private ArgumentCaptor<LocalDate> dateCaptor;

  @Captor
  private ArgumentCaptor<EmployeeId> idCaptor;

  @Captor
  private ArgumentCaptor<Hours> hoursCaptor;

  @Test
  void postMethodShouldInvokeService() throws Exception {
    String date = "2003-12-12";
    Hours hours = new Hours(12d);
    TimeCardRequestDto dto = new TimeCardRequestDto(date, hours);

    doNothing().when(this.service).addOrUpdateTimeCard(any(), any(), any());

    mockMvc.perform(post(URL, ID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated());

    verify(this.service, times(1))
        .addOrUpdateTimeCard(idCaptor.capture(), dateCaptor.capture(), hoursCaptor.capture());
    assertEquals(ID, idCaptor.getValue().toString());
    assertEquals(date, dateCaptor.getValue().toString());
    assertEquals(hours, hoursCaptor.getValue());
  }

  @Test
  void postMethodWithInvalidDateFormatShouldThrowBadRequest() throws Exception {
    String date = "2003/12/12";
    Hours hours = new Hours(12d);
    TimeCardRequestDto dto = new TimeCardRequestDto(date, hours);

    mockMvc.perform(post(URL, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());

    verify(this.service, times(0)).addOrUpdateTimeCard(any(), any(), any());
  }

  @Test
  void postMethodWithInvalidDateFormatShouldThrowBadRequest2() throws Exception {
    String date = "15-12-2003";
    Hours hours = new Hours(12d);
    TimeCardRequestDto dto = new TimeCardRequestDto(date, hours);

    mockMvc.perform(post(URL, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());

    verify(this.service, times(0)).addOrUpdateTimeCard(any(), any(), any());
  }

  @Test
  void postMethodWithNullDateShouldThrowBadRequest() throws Exception {
    Hours hours = new Hours(12d);
    TimeCardRequestDto dto = new TimeCardRequestDto(null, hours);

    mockMvc.perform(post(URL, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());

    verify(this.service, times(0)).addOrUpdateTimeCard(any(), any(), any());
  }

  @Test
  void postMethodWithNullHoursShouldThrowBadRequest() throws Exception {
    String date = "2003/12/12";
    TimeCardRequestDto dto = new TimeCardRequestDto(date, null);

    mockMvc.perform(post(URL, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());

    verify(this.service, times(0)).addOrUpdateTimeCard(any(), any(), any());
  }
}