package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.EmployeeCompensationDto;
import com.mindex.challenge.exception.CompensationNotFoundException;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mindex.challenge.common.utils.CustomDateUtils.convertFromUtcToLocalEffectiveDate;

/**
 * This class aims to provide the compensation service business functionality
 */
@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
    private final CompensationRepository compensationRepository;
    private final EmployeeRepository employeeRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CompensationServiceImpl(CompensationRepository compensationRepository,
                                   EmployeeRepository employeeRepository, MongoTemplate mongoTemplate) {
        this.compensationRepository = compensationRepository;
        this.employeeRepository = employeeRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * This method creates a compensation for the given employee Id, salary
     *
     * @param compensation the compensation to save for the given employee id
     * @return the created compensation
     */
    @Override
    public Compensation create(Compensation compensation) {
        //just insert
        Compensation savedCompensation = compensationRepository.insert(compensation);
        LOG.debug("savedCompensation: [{}]", savedCompensation);
        return compensation;
    }

    /**
     * This method updates the compensation for an employee if it exists
     *
     * @param compensation to update
     * @return the updated compensation if it exists
     */
    @Override
    public Compensation update(Compensation compensation) {
        Query query = new Query().addCriteria(Criteria.where("employeeId").is(compensation.getEmployeeId()));
        Update updateDefinition = new Update()
                .set("salary", compensation.getSalary())
                .set("effectiveDate", compensation.getEffectiveDate());
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true).upsert(false);
        Compensation savedCompensation  = mongoTemplate.findAndModify(query, updateDefinition, findAndModifyOptions, Compensation.class);
        // Compensation savedCompensation = compensationRepository.save(compensation);
         LOG.debug("savedCompensation: [{}]", savedCompensation);
        return savedCompensation;
    }

    /**
     * Reads the compensation for the given employee ID
     *
     * @param employeeId the ID of the employee
     * @return the compensation for the employee
     * @throws CompensationNotFoundException if the compensation for the employee is not found
     */
    @Override
    public EmployeeCompensationDto read(String employeeId, String timezone) {
        EmployeeCompensationDto employeeCompensationDto = new EmployeeCompensationDto();

        List<Compensation> compensationList = compensationRepository.findByEmployeeId(employeeId);
        if (CollectionUtils.isEmpty(compensationList)) {
            //we can throw an Exception or just return an empty dto, depends on requirements
            LOG.debug("There are no compensations available for the employee id [{}]", employeeId);
            throw new CompensationNotFoundException("No compensation found for the employee id: " + employeeId);
        } else {
            //Set the compensations for the employee
            employeeCompensationDto.setCompensationList(compensationList);
            //Cycle through the effective dates that are in UTC and render in the timezone favorable to the client
            setEffectiveDates(compensationList, ZoneId.of(timezone));
        }
        return employeeCompensationDto;
    }

    /*
     * Cycle through every compensation and convert the utc date to client favorable effective date
     */
    private void setEffectiveDates(List<Compensation> compensationList, ZoneId zoneId) {
        for (Compensation compensation : compensationList) {
            LocalDate utcEffectiveDate = compensation.getEffectiveDate();
            LocalDate localEffectiveDate = convertFromUtcToLocalEffectiveDate(utcEffectiveDate, zoneId);
            compensation.setEffectiveDate(localEffectiveDate);
        }
    }

}