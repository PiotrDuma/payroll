package com.github.PiotrDuma.payroll.domain.employee;

import com.github.PiotrDuma.payroll.common.address.Address;
import com.github.PiotrDuma.payroll.common.address.AddressConverter;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeId;
import com.github.PiotrDuma.payroll.common.employeeId.EmployeeIdConverter;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeDto;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeName;
import com.github.PiotrDuma.payroll.domain.employee.api.EmployeeResponse;
import com.github.PiotrDuma.payroll.domain.payment.classification.AbstractPaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.classification.PaymentClassification;
import com.github.PiotrDuma.payroll.domain.payment.method.AbstractPaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.method.api.PaymentMethod;
import com.github.PiotrDuma.payroll.domain.payment.schedule.AbstractPaymentScheduleEntity;
import com.github.PiotrDuma.payroll.domain.payment.schedule.api.PaymentSchedule;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@IdClass(EmployeeId.class)
@Entity
@Table(name = "employee")
class Employee implements EmployeeResponse {
  private static final Logger log = LoggerFactory.getLogger(Employee.class);

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @Convert(converter = EmployeeNameConverter.class)
  private EmployeeName name;
  @Convert(converter = AddressConverter.class)
  private Address address;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,
      targetEntity = AbstractPaymentScheduleEntity.class)
  private PaymentSchedule schedule;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,
      targetEntity = AbstractPaymentClassification.class)
  private PaymentClassification paymentClassification;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,
      targetEntity = AbstractPaymentMethod.class)
  private PaymentMethod paymentMethod;

  protected Employee() {
  }

  protected Employee(EmployeeName name, Address address) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.address = address;
    this.schedule = null;
    this.paymentClassification = null;
    this.paymentMethod = null;
  }

  public EmployeeId getId() {
//    return id;
    return new EmployeeId(this.id);
  }

  public EmployeeName getName() {
    return name;
  }

  public void setName(EmployeeName name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public PaymentSchedule getSchedule() {
    if(this.schedule == null){
      log.error("Employee schedule reference: null");
      throw new RuntimeException("Missing employee's schedule method");
    }
    return schedule;
  }

  public void setSchedule(PaymentSchedule schedule) {
    this.schedule = schedule;
  }

  public PaymentClassification getPaymentClassification() {
    if(this.paymentClassification == null){
      log.error("Employee classification method reference: null");
      throw new RuntimeException("Missing employee's payment classification method");
    }
    return paymentClassification;
  }

  public void setPaymentClassification(PaymentClassification paymentClassification) {
    this.paymentClassification = paymentClassification;
  }

  public PaymentMethod getPaymentMethod() {
    if(this.paymentMethod == null){
      log.error("Employee payment method reference: null");
      throw new RuntimeException("Missing employee's payment method");
    }
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  @Override
  public EmployeeDto toDto() {
    return new EmployeeDto(this.id.toString(), this.name.getName(), this.address.getAddress());
  }
}
