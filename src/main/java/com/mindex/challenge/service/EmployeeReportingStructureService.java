package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

/**
 * This interface aims to provide reporting behavior
 */
public interface EmployeeReportingStructureService {
  /**
   * Fetch employee reporting structure in the hierarchy
   * @param employeeId employee id of the employee
   * @return reporting structure hierarchy
   */
  ReportingStructure getEmployeeReportingStructureById(String employeeId);

}
