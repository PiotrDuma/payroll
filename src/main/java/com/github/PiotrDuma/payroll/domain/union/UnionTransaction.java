package com.github.PiotrDuma.payroll.domain.union;

interface UnionTransaction {
  int ADD_UNION = 1;
  int RECORD_MEMBERSHIP = 2;
  int UNDO_MEMBERSHIP = 3;
  int CHARGE_MEMBERS = 4;
  int COUNT_CHARGES = 5;

  Object execute();
}
