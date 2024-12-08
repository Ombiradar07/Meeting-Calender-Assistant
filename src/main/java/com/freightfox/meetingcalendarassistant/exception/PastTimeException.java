package com.freightfox.meetingcalendarassistant.exception;

public class PastTimeException extends RuntimeException {
    public PastTimeException(String message) {
        super(message);
    }
}
