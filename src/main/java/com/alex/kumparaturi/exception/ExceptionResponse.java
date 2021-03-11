package com.alex.kumparaturi.exception;

import java.time.Instant;
import java.util.Date;

public class ExceptionResponse<T> {
     private Instant timestamp;
     private String message;
     private T details;

    public ExceptionResponse() {
    }

    public ExceptionResponse(Instant timestamp, String message, T details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDetails() {
        return details;
    }

    public void setDetails(T details) {
        this.details = details;
    }
}
