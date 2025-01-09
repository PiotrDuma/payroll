package com.github.PiotrDuma.payroll.services.payday;

import com.github.PiotrDuma.payroll.services.payday.api.PaydayTransaction;
import java.time.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PaydayController {
  private static final Logger log = LoggerFactory.getLogger(PaydayController.class);
  private static final String MESSAGE = "/payday request run at %s";
  private final PaydayTransaction paydayTransaction;
  private final Clock clock;

  @Autowired
  public PaydayController(PaydayTransaction paydayTransaction, Clock clock) {
    this.paydayTransaction = paydayTransaction;
    this.clock = clock;
  }

  @PostMapping("/payday")
  public void run() {
    log.info(String.format(MESSAGE, clock.instant().toString()));
    this.paydayTransaction.execute();
  }
}
