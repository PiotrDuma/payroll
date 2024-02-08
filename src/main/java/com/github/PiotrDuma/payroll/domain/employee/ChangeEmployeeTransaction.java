package com.github.PiotrDuma.payroll.domain.employee;

interface ChangeEmployeeTransaction {
  int NAME = 1;
  int ADDRESS = 2;
  int HOURLY_CLASSIFICATION = 3;
  int SALARIED_CLASSIFICATION = 4;
  int COMMISSIONED_CLASSIFICATION = 5;
  int HOLD_PAYMENT = 6;
  int DIRECT_PAYMENT = 7;
  int MAIL_PAYMENT = 8;

  Object execute();
}
