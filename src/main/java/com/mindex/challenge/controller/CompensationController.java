package com.mindex.challenge.controller;

import com.mindex.challenge.controller.validator.CompensationValidator;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.EmployeeCompensationDto;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.mindex.challenge.common.utils.CustomDateUtils.getTimezoneFromClient;
import static com.mindex.challenge.common.utils.CustomDateUtils.setEffectiveDateToUtcFromHeaders;

/**
 * This class aims to provide REST endpoints for Compensation for an employee(s)
 */
@RestController
public class CompensationController {


    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);
    private final CompensationValidator compensationValidator;
    private CompensationService compensationService;

    @Autowired
    public CompensationController(CompensationService compensationService, CompensationValidator compensationValidator) {
        this.compensationService = compensationService;
        this.compensationValidator = compensationValidator;
    }

    /**
     * Fetch the compensation(s) for the given employee id
     *
     * @param id       the employee id
     * @param timezone optional timezone from the client
     * @param request  the http request
     * @return compensation of given employee
     */
    @GetMapping("/compensation/employee/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> readCompensationByEmployeeId(@PathVariable @NotNull String id,
                                                          @RequestHeader(value = "timezone", required = false)
                                                          String timezone,
                                                          HttpServletRequest request) {

        LOG.debug("Received employee compensation read request for id [{}]", id);
        Errors result = new BeanPropertyBindingResult(id, Compensation.class.getName());
        compensationValidator.validateId(id, Compensation.class,result);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        EmployeeCompensationDto employeeCompensationDto = compensationService.read(id, getTimezoneFromClient(timezone, request));

        return ResponseEntity.ok(employeeCompensationDto);
    }

    /**
     * Create compensation for the employee
     *
     * @param compensation the request body
     * @param timezone     optional timezone passed by client
     * @param request      the http request
     * @return the created compensation
     */
    @PostMapping("/compensation")
    public ResponseEntity<?> createCompensation(@RequestBody Compensation compensation,
                                                @RequestHeader(value = "timezone", required = false) String timezone,
                                                HttpServletRequest request) {
        LOG.debug("Received compensation creation request [{}]", compensation);
        Errors result = new BeanPropertyBindingResult(compensation, Compensation.class.getName());

        compensationValidator.validate(compensation, result);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
        }
        //Databases ideally should store dates in UTC so that it is agnostic to timezones
        // when retrieved from databases by clients
        LocalDate effectiveDateInUtc = setEffectiveDateToUtcFromHeaders(compensation.getEffectiveDate(), timezone, request);
        compensation.setEffectiveDate(effectiveDateInUtc);
        Compensation compensation1 = compensationService.create(compensation);
        return new ResponseEntity<>(compensation1, HttpStatus.CREATED);
      //  return compensationService.create(compensation);
    }


    /* Not part of requirements but usually is needed in such use cases,
       this can be removed on code review or confirmation of business requirements
       This endpoint enables updating the compensation of a given employee
       Note: As this is not a specified requirement, this update

       */

    /**
     * Update the compensation for a given employee
     *
     * @param compensation the request body
     * @param timezone     optional timezone passed by client
     * @param request      the http request
     * @return the updated compensation
     */
    @PutMapping("/compensation")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateCompensation(@NotNull @RequestBody @Validated Compensation compensation,
                                                @RequestHeader(value = "timezone", required = false) String timezone,
                                                HttpServletRequest request, BindingResult result) {
        LOG.debug("Received compensation update request [{}]", compensation);
        compensationValidator.validate(compensation, result);
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid compensation data");
        }
        LocalDate effectiveDateInUtc = setEffectiveDateToUtcFromHeaders(compensation.getEffectiveDate(), timezone, request);
        compensation.setEffectiveDate(effectiveDateInUtc);
        return new ResponseEntity<>(compensationService.update(compensation), HttpStatus.OK);
    }

}