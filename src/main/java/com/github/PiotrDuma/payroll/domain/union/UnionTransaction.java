package com.github.PiotrDuma.payroll.domain.union;

interface UnionTransaction {
  int ADD_UNION = 1;
  int RECORD_MEMBERSHIP = 2;

  Object execute();
}
