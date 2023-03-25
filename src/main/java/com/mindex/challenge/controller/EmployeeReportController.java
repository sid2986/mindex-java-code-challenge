package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeReportController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeReportController.class);

    @Autowired
    EmployeeReportingStructureService employeeReportingStructureService;

    /**
     * Return the reporting structure of the employees reporting to the employee having employee id
     * @param id which is employee id
     * @return employee reporting structure reporting to the passed employee
     */
    @GetMapping("employee/{id}/reporting-structure")
    public ReportingStructure getReportingStructureById(@PathVariable String id){

        LOG.debug("Fetch reporting structure for employeeId [{}]", id);

        return employeeReportingStructureService.getEmployeeReportingStructureById(id);
    }




}
