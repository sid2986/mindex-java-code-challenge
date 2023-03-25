package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * This class would be used for storing compensation
 */
@Document(collection = "compensation")
public class Compensation {
    public void setId(String id) {
        this.id = id;
    }

    @Id
    private String id;

    private String employeeId;


    private BigDecimal salary;

    //As there is no clear date formatting specifications, just keeping it simple as of now

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    public Compensation(String id, String employeeId, BigDecimal salary, LocalDate effectiveDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }
    public Compensation() {
    }

    public String getId() {
        return id;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }


    @Override
    public String toString() {
        return "Compensation{" +
                "id='" + id + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", salary=" + salary +
                ", effectiveDate=" + effectiveDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compensation)) return false;
        Compensation that = (Compensation) o;
        return getId().equals(that.getId()) && getEmployeeId().equals(that.getEmployeeId()) && getSalary().equals(that.getSalary()) && getEffectiveDate().equals(that.getEffectiveDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmployeeId(), getSalary(), getEffectiveDate());
    }
}
