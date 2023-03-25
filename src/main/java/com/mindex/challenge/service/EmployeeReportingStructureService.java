package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

public interface EmployeeReportingStructureService {

  /**
   * Fetch employee reporting structure in the hierarchy
   * @param employeeId employee id of the employee
   * @return reporting structure hierarchy
   */
  ReportingStructure getEmployeeReportingStructureById(String employeeId);

}
