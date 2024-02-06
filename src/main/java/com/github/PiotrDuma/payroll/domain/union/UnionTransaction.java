package com.github.PiotrDuma.payroll.domain.union;

interface UnionTransaction {
  int ADD_UNION = 1;

  Object execute();
}
