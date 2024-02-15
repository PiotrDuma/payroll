package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.amount.Amount;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeIdConverter;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
import com.github.PiotrDuma.payroll.exception.InvalidArgumentException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "union_entity")
class UnionEntity {
  private static final String REMOVE_EXCEPTION = "Employee %s is not a member of union(%s)";
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  private String name;
  @ElementCollection
  @CollectionTable(name = "members", joinColumns = @JoinColumn(name = "union_id"))
  @Column(name = "member", nullable = false)
  @Convert(converter = EmployeeIdConverter.class)
  private Set<EmployeeId> members;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UnionCharge> charges;

  protected UnionEntity() {
  }

  protected UnionEntity(String name) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.members = new HashSet<>();
    this.charges = new LinkedList<>();
  }

  protected UUID getId() {
    return id;
  }

  protected String getName() {
    return name;
  }

  protected boolean removeMembership(EmployeeId employeeId){
    if(!this.members.contains(employeeId)){
      throw new InvalidArgumentException(String.format(REMOVE_EXCEPTION, employeeId, this.id));
    }
    try{
      return this.members.remove(employeeId);
    }catch(Exception e){
      throw new RuntimeException("Union membership delete exception");
    }
  }

  protected Set<EmployeeId> getMembers() {
    return members;
  }

  protected void addMember(EmployeeId memberId) {
    this.members.add(memberId);
  }

  protected boolean isMember(EmployeeId employeeId){
    return this.members.contains(employeeId);
  }

  protected UnionCharge addMembersCharge(Amount amount, LocalDate date){
    UnionCharge charge = new UnionCharge(amount, date);
    this.charges.add(charge);
    return charge;
  }

  public List<UnionCharge> getCharges() {
    return charges;
  }

  protected UnionDto toDto(){
    return new UnionDto(this.id, this.name);
  }

  @Override
  public String toString() {
    return "UnionEntity{" +
        "id=" + id +
        ", name=" + name +
        ", members= {" + members +
        "}, charges= {" + charges +
        "}}";
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof UnionEntity o)){
      return false;
    }
    return o.getId().equals(id) && o.getName().equals(name) && o.getMembers().equals(members) &&
        o.getCharges().equals(charges);
  }
}
