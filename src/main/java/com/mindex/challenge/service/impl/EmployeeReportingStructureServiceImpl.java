package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exception.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

@Service
public class EmployeeReportingStructureServiceImpl implements EmployeeReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeReportingStructureServiceImpl.class);
    @Autowired
    private EmployeeRepository employeeRepository;

    /*
     * BFS traversal as hierarchies can be deep and is a standard traversal technique
     * Get the total direct and indirect reports for a given employee
     * @param employee
     * @return
     */
     private ReportingStructure getNumberOfEmployeeReports(Employee employee) {
        // Initialize numberOfReports to 0
        int numberOfReports = 0;
        // Initialize a Deque to store employees
        Deque<Employee> deque = new LinkedList<>();
        // Add the given employee to the Deque
        deque.add(employee);

        // Loop through the Deque while it's not empty
        while (!deque.isEmpty()) {
            // Remove the first employee from the Deque
            Employee currentEmployee = deque.removeFirst();
            // Check if the current employee has direct reports
            if (currentEmployee.getDirectReports() != null) {
                // Increment numberOfReports by the size of the direct reports list
                numberOfReports += currentEmployee.getDirectReports().size();
                // Add all the current employee's direct reports to the back of the Deque
                deque.addAll(currentEmployee.getDirectReports());
            }
        }
        // Set the number of reports for the given employee
        employee.setNumberOfReports(numberOfReports);
        LOG.debug("Number of reports for employee [{}]", employee.getNumberOfReports());
        // Return a new ReportingStructure object with the updated employee data
        return new ReportingStructure(employee);
    }

    /**
     * Fetch employee reporting structure in the hierarchy for the given employee id
     *
     * @param employeeId employee id of the employee
     * @return reporting structure hierarchy for the given employee id
     */
    @Override
    public ReportingStructure getEmployeeReportingStructureById(String employeeId) {
        // Get the employee with the specified ID from the repository
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (Objects.isNull(employee)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + employeeId);
        }
        return getNumberOfEmployeeReports(employee);
    }

}






