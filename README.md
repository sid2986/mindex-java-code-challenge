## Solution Summary

This project is a good way to test software engineering skills. It was quite enjoyable to work on the tasks that at first
glance seem to be simple. The requirements almost appeared to be how PO/Business/Clients communicate sometimes, and it’s 
the onus of the leads & devs & architects to elicit the requirements and drill it down into a technical vision.

### Task 1

Here, the challenge did make me think a bit. The requirements were simple. However, the fun part was to think 
as to picking the solution to go for considering that this would be memory intensive. One of the ideas was to 
go with recursion, however, for a hierarchy or a tree, it’s generally not a good idea unless one has 
oodles of resources at disposal. I went with an iterative BFS approach which is one of the standards for such traversals.

#### Assumptions:
1) Based on how  reporting or downstream systems consume data, I determined that the template of the json structure for
the employees is concise and spaced out well. The general ask was to give the sum of direct & indirect reporters. 
Therefore, I have provided the sum of the direct reports at every employee level as that is what seems logical 
when one wants to view a report, they would want to see drilled down values from the current level of the hierarchy.
2) I went with a separate controller for the Reporting to adhere to the principle of separation of concerns which
is vital to maintenance and clean code.

### Task 2:

The task here had a slight twist with the date part being tricky. 
A system should be able to pass in a date in any timezone as the client wishes, if not, then system should decide
for storage and retrieval to keep consistency between view and storage. Also, db should store in UTC format per standards.

#### Assumptions:

1) Used simple yyyy-MM-dd format though I was temped to go with timestamp which has been a norm in all my experiences.
2) Optional parameter provided in case a client wants to provide it, a good to have usually.
3) Provided Update controller as well for convenience.

### General:
1) Added basic validation and testing.
2) Documentation, commenting, general utility methods etc.
3) Postman collections
4) Did not implement any global exception handling as that would be an overkill for a small project. Only simple custom
 exception handling.




# Coding Challenge
## What's Provided
A simple [Spring Boot](https://projects.spring.io/spring-boot/) web application has been created and bootstrapped 
with data. The application contains information about all employees at a company. On application start-up, an in-memory 
Mongo database is bootstrapped with a serialized snapshot of the database. While the application runs, the data may be
accessed and mutated in the database without impacting the snapshot.

### How to Run
The application may be executed by running `gradlew bootRun`.

### How to Use
The following endpoints are available to use:
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/employee
    * PAYLOAD: Employee
    * RESPONSE: Employee
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}
    * RESPONSE: Employee
* UPDATE
    * HTTP Method: PUT 
    * URL: localhost:8080/employee/{id}
    * PAYLOAD: Employee
    * RESPONSE: Employee
```
The Employee has a JSON schema of:
```json
{
  "type":"Employee",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
          "type": "string"
    },
    "position": {
          "type": "string"
    },
    "department": {
          "type": "string"
    },
    "directReports": {
      "type": "array",
      "items" : "string"
    }
  }
}
```
For all endpoints that require an "id" in the URL, this is the "employeeId" field.

## What to Implement
Clone or download the repository, do not fork it.

### Task 1
Create a new type, ReportingStructure, that has two properties: employee and numberOfReports.

For the field "numberOfReports", this should equal the total number of reports under a given employee. The number of 
reports is determined to be the number of directReports for an employee and all of their distinct reports. For example, 
given the following employee structure:
```
                    John Lennon
                /               \
         Paul McCartney         Ringo Starr
                               /        \
                          Pete Best     George Harrison
```
The numberOfReports for employee John Lennon (employeeId: 16a596ae-edd3-4847-99fe-c4518e82c86f) would be equal to 4. 

This new type should have a new REST endpoint created for it. This new endpoint should accept an employeeId and return 
the fully filled out ReportingStructure for the specified employeeId. The values should be computed on the fly and will 
not be persisted.

### Task 2
Create a new type, Compensation. A Compensation has the following fields: employee, salary, and effectiveDate. Create 
two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the 
Compensation from the persistence layer.

## Delivery
Please upload your results to a publicly accessible Git repo. Free ones are provided by Github and Bitbucket.
