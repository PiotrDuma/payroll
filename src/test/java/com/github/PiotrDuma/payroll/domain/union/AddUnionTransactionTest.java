package com.github.PiotrDuma.payroll.domain.union;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddUnionTransactionTest {
  private static final String NAME = "UNION NAME";
  @Mock
  private UnionAffiliationRepository repo;
  private ListAppender<ILoggingEvent> logWatcher;
  private AddUnionTransaction transaction;

  @BeforeEach
  void setup() {
    this.transaction = new AddUnionTransaction(repo, NAME);
  }

  @Test
  void shouldAddNewUnion(){
    UnionDto result = (UnionDto)this.transaction.execute();
    String expectedMessage = "Transaction executed: add Union: id: " + result.id();

    verify(this.repo, times(1)).save(any(UnionEntity.class));
    assertEquals(NAME, result.name());
  }

}