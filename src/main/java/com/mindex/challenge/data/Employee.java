package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Employee {
    @Id
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private int numberOfReports;
    // This is needed to populate details of other referencing employees
    @DBRef(lazy = true)
    private List<Employee> directReports;

    public Employee() {
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("employeeId", this.employeeId);
        map.put("firstName", this.firstName);
        map.put("lastName", this.lastName);
        map.put("position", this.position);
        map.put("department", this.department);
        return map;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return getEmployeeId().equals(employee.getEmployeeId()) && getFirstName().equals(employee.getFirstName()) && getLastName().equals(employee.getLastName()) && getPosition().equals(employee.getPosition()) && getDepartment().equals(employee.getDepartment()) && Objects.equals(getDirectReports(), employee.getDirectReports());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployeeId(), getFirstName(), getLastName(), getPosition(), getDepartment(), getDirectReports());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", directReports=" + directReports +
                '}';
    }
}
