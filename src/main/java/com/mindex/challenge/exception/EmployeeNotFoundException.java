package com.mindex.challenge.exception;

/**
 * Custom employee exception class for employee related exceptions
 */
public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}