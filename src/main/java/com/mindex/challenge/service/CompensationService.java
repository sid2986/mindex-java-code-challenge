package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

import java.util.List;

public interface CompensationService {

    /**
     * Create compensation for the given employee id
     * @return created compensation
     */
    Compensation create(Compensation compensation);

    /**
     * Update compensation for the given employee id for the given effective date
     * @param compensation the compensation to update
     * @return updated compensation
     */
    Compensation update(Compensation compensation);

    /**
     * Get compensation(s) for the given employee id
     *
     * @param employeeId the employeeId for fetching the compensation(s)
     * @param timezone   the timezone as passed by client
     * @return compensation the compensation of the employee id
     */
    List<Compensation> read(String employeeId, String timezone);
}
