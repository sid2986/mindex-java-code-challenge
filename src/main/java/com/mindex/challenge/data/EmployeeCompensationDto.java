package com.mindex.challenge.data;

import java.util.List;
import java.util.Objects;

/**
 * DTO for compensations
 */
public class EmployeeCompensationDto {

    private List<Compensation> compensationList;

    public EmployeeCompensationDto() {
    }

    public List<Compensation> getCompensationList() {
        return compensationList;
    }

    public void setCompensationList(List<Compensation> compensationList) {
        this.compensationList = compensationList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeCompensationDto)) return false;
        EmployeeCompensationDto that = (EmployeeCompensationDto) o;
        return Objects.equals(getCompensationList(), that.getCompensationList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCompensationList());
    }

    @Override
    public String toString() {
        return "EmployeeCompensationDto{" +
                "compensationList=" + compensationList +
                '}';
    }
}
