package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class UnionEntity {
  private final UUID id;
  private String name;
  // @ElementCollection
  private Set<EmployeeId> members;

  protected UnionEntity(String name) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.members = new HashSet<>();
  }

  protected UUID getId() {
    return id;
  }

  protected String getName() {
    return name;
  }

  protected boolean removeMembership(EmployeeId employeeId){
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

  protected UnionDto toDto(){
    return new UnionDto(this.id, this.name);
  }
}
