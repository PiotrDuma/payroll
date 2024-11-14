package com.github.PiotrDuma.payroll.domain.union;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.domain.union.UnionController.AmountDto;
import com.github.PiotrDuma.payroll.domain.union.UnionController.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

@WebMvcTest(controllers = UnionController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UnionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UnionAffiliationRepository repo;
  @MockBean
  private UnionAffiliationService unionService;

  @InjectMocks
  private UnionController controller;


  @Test
  void shouldInvokeAddUnion() throws Exception {
    String unionName = "unionName";
    UUID id = UUID.randomUUID();
    UnionController.UnionNameDto dto = new UnionController.UnionNameDto(unionName);
    UnionDto expected = new UnionDto(id, unionName);

    when(this.unionService.addUnion(any())).thenReturn(expected);

    ResultActions result = mockMvc.perform(post("/unions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    result.andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id",
            Matchers.containsString(expected.id().toString())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.name", Matchers.containsString(expected.name())))
        .andExpect(MockMvcResultMatchers.redirectedUrlPattern("http://*/unions/" + id));
  }

  @Test
  void requestByIdShouldReturnUnionDto() throws Exception {
    String unionName = "unionName";
    UUID id = UUID.randomUUID();
    UnionDto expected = new UnionDto(id, unionName);
    UnionEntity union = mock(UnionEntity.class);

    when(this.repo.findById(any())).thenReturn(Optional.of(union));
    when(union.toDto()).thenReturn(expected);

    ResultActions result = mockMvc.perform(get("/unions/" + id));

    result.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id",
            Matchers.containsString(expected.id().toString())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.name", Matchers.containsString(expected.name())));
  }

  @Test
  void requestUnionByIdShouldThrowNotFoundWhenIsNotFound() throws Exception {
    String message = "message";
    UUID id = UUID.randomUUID();

    doThrow(new ResourceNotFoundException(message)).when(this.repo).findById(any());

    ResultActions result = mockMvc.perform(get("/unions/" + id))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  void requestUnionByIdShouldThrowBadRequestWhenIdIsInvalid() throws Exception {

    ResultActions result = mockMvc.perform(get("/unions/" + "123"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void shouldReturnUnionsDtoList() throws Exception {
    String unionName = "unionName";
    String unionName1 = "unionName1";
    UUID id = UUID.randomUUID();
    UUID id1 = UUID.randomUUID();
    UnionEntity union = mock(UnionEntity.class);
    UnionEntity union1 = mock(UnionEntity.class);

    UnionDto expected = new UnionDto(id, unionName);
    UnionDto expected1 = new UnionDto(id1, unionName1);

    when(this.repo.findAll()).thenReturn(List.of(union, union1));
    when(union.toDto()).thenReturn(expected);
    when(union1.toDto()).thenReturn(expected1);

    ResultActions result = mockMvc.perform(get("/unions"));

    result.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id",
            Matchers.containsString(expected.id().toString())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[0].name", Matchers.containsString(expected.name())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id",
            Matchers.containsString(expected1.id().toString())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[1].name", Matchers.containsString(expected1.name())));
  }

  @Test
  void shouldInvokeChargeOnUnion() throws Exception {
    UUID id = UUID.randomUUID();
    AmountDto amount = new AmountDto(1d);
    ArgumentCaptor<UUID> providedId = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<Amount> providedAmount = ArgumentCaptor.forClass(Amount.class);

    ResultActions result = mockMvc.perform(post("/unions/" + id + "/charge")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(amount)));

    result.andExpect(MockMvcResultMatchers.status().isNoContent());
    verify(this.unionService, times(1))
        .chargeMembers(providedId.capture(), providedAmount.capture(), any());

    Assertions.assertEquals(id, providedId.getValue());
    Assertions.assertTrue(
        providedAmount.getValue().getAmount().equals(BigDecimal.valueOf(amount.amount())));
  }

  @Test
  void chargeShouldThrowBadRequestWhenUnionIdIsInvalid() throws Exception {
    AmountDto amount = new AmountDto(1d);

    ResultActions result = mockMvc.perform(post("/unions/" + "123" + "/charge")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(amount)));

    result.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void chargeShouldThrowBadRequestWhenAmountIsNegative() throws Exception {
    String expectedMessage = "Amount cannot be lower than 0";
    UUID id = UUID.randomUUID();
    AmountDto amount = new AmountDto(-11d);

    ResultActions result = mockMvc.perform(post("/unions/" + id + "/charge")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(amount)));

    result.andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage",
            Matchers.containsString(expectedMessage)));
  }

  @Test
  void shouldInvokeAddMember() throws Exception {
    UUID unionId = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    EmployeeDto dto = new EmployeeDto(employeeId.toString());

    ArgumentCaptor<UUID> providedUnionId = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<EmployeeId> providedEmployeeId = ArgumentCaptor.forClass(EmployeeId.class);

    ResultActions result = mockMvc.perform(post("/unions/" + unionId + "/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    result.andExpect(MockMvcResultMatchers.status().isNoContent());
    verify(this.unionService, times(1))
        .recordMembership(providedUnionId.capture(), providedEmployeeId.capture());

    Assertions.assertEquals(unionId, providedUnionId.getValue());
    Assertions.assertEquals(employeeId, providedEmployeeId.getValue().getId());
  }

  @Test
  void addMemberShouldThrowBadRequestWhenInvalidUnionIdFormat() throws Exception {
    UUID unionId = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    EmployeeDto dto = new EmployeeDto(employeeId.toString());

    ResultActions result = mockMvc.perform(post("/unions/" + "123" + "/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    result.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void addMemberShouldThrowBadRequestWhenInvalidEmployeeIdFormat() throws Exception {
    UUID unionId = UUID.randomUUID();
    EmployeeDto dto = new EmployeeDto("123");

    ResultActions result = mockMvc.perform(post("/unions/" + unionId + "/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    result.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void shouldInvokeUndoMembership() throws Exception {
    UUID unionId = UUID.randomUUID();
    UUID employeeId = UUID.randomUUID();
    EmployeeDto dto = new EmployeeDto(employeeId.toString());

    ArgumentCaptor<UUID> providedUnionId = ArgumentCaptor.forClass(UUID.class);
    ArgumentCaptor<EmployeeId> providedEmployeeId = ArgumentCaptor.forClass(EmployeeId.class);

    ResultActions result = mockMvc.perform(post("/unions/" + unionId + "/undo")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    result.andExpect(MockMvcResultMatchers.status().isNoContent());
    verify(this.unionService, times(1))
        .undoMembershipAffiliation(providedUnionId.capture(), providedEmployeeId.capture());

    Assertions.assertEquals(unionId, providedUnionId.getValue());
    Assertions.assertEquals(employeeId, providedEmployeeId.getValue().getId());
  }

  @Test
  void undoMembershipShouldThrowBadRequestWhenUnionIdIsInvalid() throws Exception {
    UUID employeeId = UUID.randomUUID();
    EmployeeDto dto = new EmployeeDto(employeeId.toString());

    ResultActions result = mockMvc.perform(post("/unions/" + "123" + "/undo")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    result.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void undoMembershipShouldThrowBadRequestWhenEmployeeIdIsInvalid() throws Exception {
    UUID unionId = UUID.randomUUID();
    EmployeeDto dto = new EmployeeDto("123");

    ResultActions result = mockMvc.perform(post("/unions/" + unionId + "/undo")
        .contentType(MediaType.APPLICATION_JSON)
        .content(ObjectMapperProvider.createJson().writeValueAsString(dto)));

    result.andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}