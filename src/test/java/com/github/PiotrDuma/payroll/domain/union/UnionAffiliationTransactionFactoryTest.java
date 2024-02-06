package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnionAffiliationTransactionFactoryTest {

  @Mock
  private UnionAffiliationRepository repo;

  @InjectMocks
  private UnionAffiliationTransactionFactory factory;

  @Test
  void shouldReturnAddUnionTransaction(){
    String unionName = "UNION NAME";
    int transactionCode = UnionTransaction.ADD_UNION;
    UnionTransaction result = this.factory.create(transactionCode, unionName);

    assertTrue(result instanceof AddUnionTransaction);
  }
}