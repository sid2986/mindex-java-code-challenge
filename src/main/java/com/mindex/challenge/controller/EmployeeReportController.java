package com.mindex.challenge.controller;

import com.mindex.challenge.controller.validator.CommonValidator;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * This Controller aims to provide Reporting based APIs
 */
@RestController
public class EmployeeReportController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeReportController.class);

    @Autowired
    EmployeeReportingStructureService employeeReportingStructureService;

    private final CommonValidator commonValidator;

    @Autowired
    public EmployeeReportController(CommonValidator compensationValidator) {
        this.commonValidator = compensationValidator;
    }

    /**
     * Return the reporting structure of the employees reporting to the employee having employee id
     * @param id which is employee id
     * @return employee reporting structure reporting to the passed employee
     */
    @GetMapping("employee/{id}/reporting-structure")
    public ResponseEntity<?> getReportingStructureById(@PathVariable String id){
        Errors result = new BeanPropertyBindingResult(id, ReportingStructure.class.getName());
        commonValidator.extractedString(id, result,id);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        LOG.debug("Fetch reporting structure for employeeId [{}]", id);
       return ResponseEntity.ok(employeeReportingStructureService.getEmployeeReportingStructureById(id));
    }



}
