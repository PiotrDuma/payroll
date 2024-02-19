package com.github.PiotrDuma.payroll.services.payday;

import com.github.PiotrDuma.payroll.domain.employee.api.ReceiveEmployee;
import com.github.PiotrDuma.payroll.domain.union.api.UnionAffiliationService;
import com.github.PiotrDuma.payroll.services.payday.api.PaydayTransaction;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Tag("IntegrationTest")
@ActiveProfiles("test")
class PaydayTransactionIntegrationTest {
  private static final ZonedDateTime NOW = ZonedDateTime.of(2000, 1, 1,
      16, 5, 12, 0, ZoneId.of("UTC"));
  @Autowired
  private ReceiveEmployee receiveEmployee;
  @Autowired
  private UnionAffiliationService unionAffiliationService;


  @Autowired
  private PaycheckRepository repo;

  private PaydayTransaction transaction;

  @BeforeEach
  void setUp(){
    this.transaction = new PaydayTransactionService(receiveEmployee, unionAffiliationService,
        clock(), repo);
  }

  @Bean
  @Primary
  private Clock clock(){
    return Clock.fixed(NOW.toInstant(), NOW.getZone());
  }
}