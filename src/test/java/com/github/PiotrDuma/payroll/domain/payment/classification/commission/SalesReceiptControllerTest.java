package com.github.PiotrDuma.payroll.domain.payment.classification.commission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.SalesReceiptController.SalesReceiptRequestDto;
import com.github.PiotrDuma.payroll.domain.payment.classification.commission.api.SalesReceiptProvider;
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

@WebMvcTest(SalesReceiptController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class SalesReceiptControllerTest {
  private static final String URL = "/employees/{id}/receipts";
  private static final String ID = UUID.randomUUID().toString();

  @MockBean
  private SalesReceiptProvider service;

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @InjectMocks
  private SalesReceiptController controller;

  @Captor
  private ArgumentCaptor<LocalDate> dateCaptor;

  @Captor
  private ArgumentCaptor<EmployeeId> idCaptor;

  @Captor
  private ArgumentCaptor<Amount> amountCaptor;

  @Test
  void postMethodShouldInvokeService() throws Exception {
    String date = "2003-12-12";
    Amount amount = new Amount(1234d);
    SalesReceiptRequestDto dto = new SalesReceiptRequestDto(date, amount);

    doNothing().when(this.service).addSalesReceipt(any(), any(), any());

    mockMvc.perform(post(URL, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated());

    verify(this.service, times(1))
        .addSalesReceipt(idCaptor.capture(), dateCaptor.capture(), amountCaptor.capture());
    assertEquals(ID, idCaptor.getValue().toString());
    assertEquals(date, dateCaptor.getValue().toString());
    assertEquals(amount, amountCaptor.getValue());
  }

  @Test
  void postMethodWithInvalidDateFormatShouldThrowBadRequest() throws Exception{
    String date = "2003/12/12";
    Amount amount = new Amount(1234d);
    SalesReceiptRequestDto dto = new SalesReceiptRequestDto(date, amount);

    doNothing().when(this.service).addSalesReceipt(any(), any(), any());

    mockMvc.perform(post(URL, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());

    verify(this.service, times(0))
        .addSalesReceipt(any(), any(), any());
  }

  @Test
  void postMethodWithInvalidDateFormatShouldThrowBadRequest2() throws Exception{
    String date = "30-10-2023";
    Amount amount = new Amount(1234d);
    SalesReceiptRequestDto dto = new SalesReceiptRequestDto(date, amount);

    doNothing().when(this.service).addSalesReceipt(any(), any(), any());

    mockMvc.perform(post(URL, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());

    verify(this.service, times(0))
        .addSalesReceipt(any(), any(), any());
  }

  @Test
  void postMethodWithNullDateShouldThrowBadRequest() throws Exception{
    Amount amount = new Amount(1234d);
    SalesReceiptRequestDto dto = new SalesReceiptRequestDto(null, amount);

    doNothing().when(this.service).addSalesReceipt(any(), any(), any());

    mockMvc.perform(post(URL, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());

    verify(this.service, times(0))
        .addSalesReceipt(any(), any(), any());
  }

  @Test
  void postMethodWithNullAmountShouldThrowBadRequest() throws Exception{
    String date = "2003-12-12";
    SalesReceiptRequestDto dto = new SalesReceiptRequestDto(date, null);

    doNothing().when(this.service).addSalesReceipt(any(), any(), any());

    mockMvc.perform(post(URL, ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());

    verify(this.service, times(0))
        .addSalesReceipt(any(), any(), any());
  }
}