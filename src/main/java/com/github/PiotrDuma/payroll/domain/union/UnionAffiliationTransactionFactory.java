package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.PaymentPeriod;
import com.github.PiotrDuma.payroll.domain.employee.api.model.ReceiveEmployee;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class UnionAffiliationTransactionFactory {
  private final UnionAffiliationRepository repository;
  private final ReceiveEmployee employeeRepository;

  @Autowired
  public UnionAffiliationTransactionFactory(UnionAffiliationRepository repository,
    ReceiveEmployee employeeRepository) {
    this.repository = repository;
    this.employeeRepository = employeeRepository;
  }

  public UnionTransaction create(int transactionCode, Object... params){
    return switch(transactionCode){
      case UnionTransaction.ADD_UNION -> new AddUnionTransaction(repository, (String)params[0]);
      case UnionTransaction.RECORD_MEMBERSHIP -> new RecordMembershipTransaction(repository,
           employeeRepository,(UUID)params[0], (EmployeeId)params[1]);
      case UnionTransaction.UNDO_MEMBERSHIP ->
          new UndoMembershipTransaction(repository, (UUID)params[0], (EmployeeId)params[1]);
      case UnionTransaction.CHARGE_MEMBERS -> new ChargeMembersTransaction(repository,
          (UUID)params[0], (Amount)params[1], (LocalDate)params[2]);
      case UnionTransaction.COUNT_CHARGES -> new CountMembershipChargesTransaction(repository,
          (EmployeeId)params[0], (PaymentPeriod)params[1]);
      default -> null;
    };
  }
}
