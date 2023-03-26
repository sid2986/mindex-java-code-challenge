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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void testCreateReadUpdate1() {
        ReportingStructure testReportingStructure = new ReportingStructure(testEmployee, 4);
        // Read checks from root
        ResponseEntity readReportingStructureResponse = restTemplate.getForEntity(readReportingStructureUrl,
                ReportingStructure.class, testEmployee.getEmployeeId());
        assertEquals(HttpStatus.OK, readReportingStructureResponse.getStatusCode());
        ReportingStructure readReportingStructure = (ReportingStructure) readReportingStructureResponse.getBody();
        assertNotNull(readReportingStructure);
        assertEquals(readReportingStructure.getEmployee().getEmployeeId(), testReportingStructure.getEmployee().getEmployeeId());
        assertEquals(readReportingStructure.getNumberOfReports(), testReportingStructure.getNumberOfReports());

        //Read checks from middle
        testEmployee = employeeRepository.findByEmployeeId("03aa1462-ffa9-4978-901b-7c001562cf6f");
        ReportingStructure testReportingStructureTwo = new ReportingStructure(testEmployee, 2);
        ResponseEntity readReportingStructureResponseTwo = restTemplate.getForEntity(readReportingStructureUrl,
                ReportingStructure.class, testEmployee.getEmployeeId());
        assertEquals(HttpStatus.OK, readReportingStructureResponseTwo.getStatusCode());
        ReportingStructure readReportingStructureTwo = (ReportingStructure) readReportingStructureResponseTwo.getBody();
        assertNotNull(readReportingStructureTwo);
        assertEquals(readReportingStructureTwo.getEmployee().getEmployeeId(), testReportingStructureTwo.getEmployee().getEmployeeId());
        assertEquals(readReportingStructureTwo.getNumberOfReports(), testReportingStructureTwo.getNumberOfReports());

        //Read checks at bottom
        testEmployee = employeeRepository.findByEmployeeId("c0c2293d-16bd-4603-8e08-638a9d18b22c");
        ReportingStructure testReportingStructureThree = new ReportingStructure(testEmployee, 0);
        ResponseEntity readReportingStructureResponseThree = restTemplate.getForEntity(readReportingStructureUrl,
                ReportingStructure.class, testEmployee.getEmployeeId());
        assertEquals(HttpStatus.OK, readReportingStructureResponseThree.getStatusCode());
        ReportingStructure readReportingStructureThree = (ReportingStructure) readReportingStructureResponseThree.getBody();
        assertNotNull(readReportingStructureThree);
        assertEquals(readReportingStructureThree.getEmployee().getEmployeeId(), testReportingStructureThree.getEmployee().getEmployeeId());
        assertEquals(readReportingStructureThree.getNumberOfReports(), testReportingStructureThree.getNumberOfReports());

    }

}
