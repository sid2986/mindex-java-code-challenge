/**
 * Test cases for exercising the CompensationController endpoints.
 */

package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    public static final String AMERICA_NEW_YORK = "America/New_York";
    @Autowired
    CompensationService compensationService;
    private String compensationUrl;
    private String compensationsByEmpIdUrl;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    private Employee testEmployee;

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationsByEmpIdUrl = "http://localhost:" + port + "/compensation/employee/{id}";
    }

    /**
     * Simple CRUD tests to check if the wired components and functionality asserts
     */
    @Test
    public void testCreateReadUpdateCompensation() {

        //set up test data
        Employee testEmployee = employeeService.create(setupTestEmployee());
        ZoneId estZoneId = ZoneId.of(AMERICA_NEW_YORK);
        LocalDate nowInEst = LocalDate.now(estZoneId);

        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(testEmployee.getEmployeeId());
        testCompensation.setSalary(BigDecimal.valueOf(7500.25));
        testCompensation.setEffectiveDate(nowInEst);

        ZoneId sourceTimeZone = ZoneId.of(AMERICA_NEW_YORK);
        ZonedDateTime sourceDateTime = nowInEst.atStartOfDay(sourceTimeZone);

        Instant utcInstant = sourceDateTime.withZoneSameInstant(ZoneOffset.UTC).toInstant();

        LocalDate utcDateExpected = utcInstant.atZone(ZoneOffset.UTC).toLocalDate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Compensation> requestEntity = new HttpEntity<>(testCompensation, headers);

        ResponseEntity<Compensation> responseEntity = restTemplate.postForEntity(compensationUrl, requestEntity, Compensation.class);
        Compensation createdCompensation = responseEntity.getBody();

        // Create or Insert assertions
        assertNotNull(createdCompensation);
        assertCompensationEquivalence(testCompensation, createdCompensation);
        assertEquals(utcDateExpected, createdCompensation.getEffectiveDate()); //UTC date assertion

        // Read or Query assertions

        List<Compensation> readEmployeeCompensations = restTemplate.exchange(
                compensationsByEmpIdUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Compensation>>() {},
                testEmployee.getEmployeeId()).getBody();
        assert readEmployeeCompensations != null;
        Compensation compensationToTest= readEmployeeCompensations.get(0);
        assertNotNull(readEmployeeCompensations);
        assertEmployeeIdEquivalence(readEmployeeCompensations.get(0).getEmployeeId(), testEmployee.getEmployeeId());
        assertEquals(createdCompensation.getEmployeeId(), readEmployeeCompensations.get(0).getEmployeeId());
        assertCompensationEquivalence(createdCompensation, compensationToTest);

        //assert that effective date returns in the client timezone
        assertEquals(createdCompensation.getEffectiveDate(), compensationToTest.getEffectiveDate());

        // Basic Update assertions
        readEmployeeCompensations.get(0).setSalary(BigDecimal.valueOf(8900));

        HttpHeaders headers2 = new HttpHeaders();
        HttpEntity<Compensation> entity2 = new HttpEntity<>(compensationToTest, headers2);
        headers2.setContentType(MediaType.APPLICATION_JSON);

        Compensation updatedCompensation =
                restTemplate.exchange(compensationUrl,
                        HttpMethod.PUT,
                        entity2,
                        Compensation.class).getBody();

        assertNotNull(updatedCompensation);
        assertCompensationEquivalence(compensationToTest, updatedCompensation);
    }

    /**
     * Add multiple compensation to the same employee
     * Assert that all saved and retrieved objects are available with their expected values.
     */
    @Test
    public void testReadEmployeeCompensations() {

        //set up test data
        Employee testEmployee = setupTestEmployee();
        Employee createdEmployee = employeeService.create(testEmployee);

        Compensation testCompensation1 = new Compensation();
        testCompensation1.setId("testId-2");
        testCompensation1.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation1.setSalary(BigDecimal.valueOf(59000.65));
        testCompensation1.setEffectiveDate(LocalDate.of(1990, 10, 11));
        Compensation createdCompensation1 =
                restTemplate.postForEntity(compensationUrl, testCompensation1, Compensation.class).getBody();

        assertNotNull(createdCompensation1);

        Compensation testCompensation2 = new Compensation();
        testCompensation2.setId("testId-2");
        testCompensation2.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation2.setSalary(BigDecimal.valueOf(1320202.02));
        testCompensation2.setEffectiveDate(LocalDate.of(1987, 4, 5));
        Compensation createdCompensation2 =
                restTemplate.postForEntity(compensationUrl, testCompensation2, Compensation.class).getBody();
        assertNotNull(createdCompensation2);

        // Employee id check
        List<Compensation> readEmployeeCompensations = restTemplate.exchange(
                compensationsByEmpIdUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Compensation>>() {},
                testEmployee.getEmployeeId()).getBody();
        assertNotNull(readEmployeeCompensations);
        assertEmployeeIdEquivalence(readEmployeeCompensations.get(0).getEmployeeId(), testEmployee.getEmployeeId());

        // Compensation records check
        for (Compensation compensation : readEmployeeCompensations) {

            if (compensation.getId().equals(createdCompensation1.getId())) {
                assertCompensationEquivalence(compensation, testCompensation1);
            } else if (compensation.getId().equals(createdCompensation2.getId())) {
                assertCompensationEquivalence(compensation, testCompensation2);
            }
        }
    }

    Employee setupTestEmployee() {
        testEmployee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        return testEmployee;
    }

    private void assertEmployeeIdEquivalence(String expectedEmployeeId, String actualEmployeeId) {
        assertEquals(expectedEmployeeId, actualEmployeeId);
    }

}