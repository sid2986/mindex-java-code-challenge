package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeReportingStructureService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeReportingStructureServiceImplTest {

    public static final String HTTP_LOCALHOST = "http://localhost:";
    public static final String EMPLOYEE_REPORTING_STRUCTURE_ID_URL_STUB = "employee/{id}/reporting-structure";
    private String readReportingStructureUrl;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeReportingStructureService reportStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Employee testEmployee;

    @Before
    public void setup() {
        readReportingStructureUrl = HTTP_LOCALHOST + port + EMPLOYEE_REPORTING_STRUCTURE_ID_URL_STUB;

        testEmployee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
    }

    @After
    public void teardown() {
        readReportingStructureUrl = null;
        testEmployee = null;
    }

    @Test
    public void testCreateReadUpdate() {
        ReportingStructure testReportingStructure = new ReportingStructure(testEmployee, 4);

        // Read checks
        ResponseEntity readReportingStructureResponse = restTemplate.getForEntity(readReportingStructureUrl,
                ReportingStructure.class, testEmployee.getEmployeeId());
        assertEquals(HttpStatus.OK, readReportingStructureResponse.getStatusCode());
        ReportingStructure readReportingStructure = (ReportingStructure)readReportingStructureResponse.getBody();
        assertNotNull(readReportingStructure);
        assertEquals(readReportingStructure, testReportingStructure);
    }

}
