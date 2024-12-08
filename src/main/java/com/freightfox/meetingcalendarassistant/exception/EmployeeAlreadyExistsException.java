package com.freightfox.meetingcalendarassistant.exception;

public class EmployeeAlreadyExistsException extends RuntimeException{

    public EmployeeAlreadyExistsException(String message) {
        super( message);
    }
}
