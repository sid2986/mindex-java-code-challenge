package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {

    /**
     * Finds the latest compensation for given employee id
     *
     * @param employeeId the employee to search for
     * @return the compensation associated with the employee ID, or null if not found
     */
     List<Compensation> findByEmployeeId(String employeeId);
}
