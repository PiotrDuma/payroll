package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.payroll.common.Amount;
import com.github.PiotrDuma.payroll.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChargeMembersTransactionTest {
  private static final Amount AMOUNT = new Amount(123);
  private static final LocalDate DATE = LocalDate.of(2000, 1, 2);
  @Mock
  private UnionAffiliationRepository repo;
  private ChargeMembersTransaction transaction;
  private UnionEntity union;

  @BeforeEach
  void setUp() {
    this.union = new UnionEntity("name");
    this.transaction = new ChargeMembersTransaction(repo, union.getId(), AMOUNT, DATE);
  }

  @Test
  void shouldAddNewUnionChargeToUnionObject() {
    when(this.repo.findById(any())).thenReturn(Optional.of(union));

    assertEquals(0, union.getCharges().size());
    transaction.execute();

    assertEquals(1, union.getCharges().size());
    verify(this.repo, times(1)).save(union);

    Optional<UnionCharge> result = union.getCharges().stream().findAny();
    assertTrue(result.isPresent());
    assertEquals(AMOUNT, result.get().getAmount());
    assertEquals(DATE, result.get().getDate());
  }

  @Test
  void shouldThrowResourceNotFoundWhenUnionIsNotFound() {
    String message = "Union not found";

    when(this.repo.findById(any())).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class, () -> this.transaction.execute());

    assertEquals(message, exception.getMessage());
  }
}