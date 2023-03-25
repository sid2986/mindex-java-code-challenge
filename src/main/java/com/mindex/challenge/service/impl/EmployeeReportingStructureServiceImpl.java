package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeReportingStructureServiceImpl implements EmployeeReportingStructureService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeReportingStructureServiceImpl.class);


    /**
     * Fetch employee reporting structure in the hierarchy for the given employee id
     *
     * @param employeeId employee id of the employee
     * @return reporting structure hierarchy for the given employee id
     */
    @Override
    public ReportingStructure getEmployeeReportingStructureById(String employeeId) {
        // fetch the employee by id
        Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if (Objects.isNull(employee)) {
            throw new RuntimeException("No such employee for passed employee id");
        }
        //get the total count of direct reports for the employee
        int numberOfReports = getNumberOfDirectReports(employee);
        LOG.debug("Number of reports: {}", numberOfReports);
        //return the reporting structure with number of reports calculated in above step
        return new ReportingStructure(employee, numberOfReports);
    }



    /*
     *  The getNumberOfDirectReports method calculates the number of direct
     *  and indirect reports for the given employee by iterating through all employees
     *  who report directly or indirectly to the given employee in a BFS algorithm.
     */
    private int getNumberOfDirectReports(Employee employee) {
        // Keep count of direct reports for the employee passed
        int count = 0;
        // The visited set will keep track of the employees that have been visited
        Set<String> visited = new HashSet<>();
        // The queue will store the employees that need to be visited in the next level
        Deque<Employee> queue = new ArrayDeque<>();

        // Add the employee to the queue and mark them as visited
        queue.offer(employee);
        visited.add(employee.getEmployeeId());

        // Iterate through the queue until all employees have been visited
        while (!queue.isEmpty()) {
            // Get the next employee in the queue and increment the count of direct reports
            Employee currentEmployee = queue.poll();
            count++;
            // Get the list of direct reports for the current employee
            List<Employee> directReports = currentEmployee.getDirectReports();
            // If there are direct reports, add them to the queue if they haven't been visited already
            if (directReports != null) {
                for (Employee directReport : directReports) {
                    // If the direct report hasn't been visited, add them to the queue and mark them as visited
                    if (visited.add(directReport.getEmployeeId())) {
                        queue.offer(directReport);
                    }
                }
            }
        }
        // We subtract 1 from the count since we don't want to include the original employee in the direct reporter count
        return count - 1;
    }

}

