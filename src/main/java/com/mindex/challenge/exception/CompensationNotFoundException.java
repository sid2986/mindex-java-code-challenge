package com.mindex.challenge.exception;

/**
 * Custom compensation exception class for compensation related exceptions
 */
public class CompensationNotFoundException extends RuntimeException {
    public CompensationNotFoundException(String message) {
        super(message);
    }
}