package com.mindex.challenge.data;

import java.util.*;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;
    private final List<ReportingStructure> directReports = new ArrayList<>();

    public ReportingStructure() {
    }

    public ReportingStructure(Employee rootEmployee, int numberOfReports) {
        this.employee = rootEmployee;
        this.numberOfReports = numberOfReports;
    }


    public ReportingStructure(Employee employee) {
        this.employee = employee;
        this.numberOfReports = calculateNumberOfReports(employee);
    }

    private int calculateNumberOfReports(Employee employee) {
        int numReports = 0;
        if (employee.getDirectReports() != null) {
            numReports += employee.getDirectReports().size();
            Queue<Employee> queue = new LinkedList<>(employee.getDirectReports());
            while (!queue.isEmpty()) {
                Employee directReport = queue.poll();
                if (directReport.getDirectReports() != null) {
                    numReports += directReport.getDirectReports().size();
                    queue.addAll(directReport.getDirectReports());
                }
            }
        }
        return numReports;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public List<ReportingStructure> getDirectReports() {
        return directReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportingStructure)) return false;
        ReportingStructure that = (ReportingStructure) o;
        return getNumberOfReports() == that.getNumberOfReports() && Objects.equals(getEmployee(), that.getEmployee()) && Objects.equals(getDirectReports(), that.getDirectReports());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployee(), getNumberOfReports(), getDirectReports());
    }

}
