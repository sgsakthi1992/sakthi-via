package com.practice.sakthi_via.exception;

import java.util.Date;

public final class ErrorDetails {
    final private Date timestamp;
    final private String message;
    final private String details;

    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}