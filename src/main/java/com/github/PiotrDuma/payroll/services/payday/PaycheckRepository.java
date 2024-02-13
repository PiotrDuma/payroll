package com.github.PiotrDuma.payroll.services.payday;

interface PaycheckRepository {
  PaycheckEntity save(PaycheckEntity paycheck);
}
