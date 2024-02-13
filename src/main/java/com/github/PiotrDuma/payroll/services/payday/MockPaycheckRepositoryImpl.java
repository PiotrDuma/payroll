package com.github.PiotrDuma.payroll.services.payday;

import org.springframework.stereotype.Repository;

@Repository
public class MockPaycheckRepositoryImpl implements PaycheckRepository{
  //TODO: REMOVE THIS AND IMPLEMENT INFRASTRUCTURE

  @Override
  public PaycheckEntity save(PaycheckEntity paycheck) {
    throw new RuntimeException("PaycheckRepository class not implemented");
  }
}
