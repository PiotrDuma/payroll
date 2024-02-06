package com.github.PiotrDuma.payroll.domain.union;

import com.github.PiotrDuma.payroll.common.EmployeeId;
import com.github.PiotrDuma.payroll.domain.union.api.UnionDto;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class UnionEntity {
  private final UUID id;
  private String name;
  private Set<EmployeeId> members;

  public UnionEntity(String name) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.members = new HashSet<>();
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<EmployeeId> getMembers() {
    return members;
  }

  public void addMember(EmployeeId memberId) {
    this.members.add(memberId);
  }

  public UnionDto toDto(){
    return new UnionDto(this.id, this.name);
  }
}
