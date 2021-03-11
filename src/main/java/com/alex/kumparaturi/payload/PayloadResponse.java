package com.alex.kumparaturi.payload;

import java.util.List;

public class PayloadResponse<T> {
    private T payload;
    private String status;
    private String message;

    public PayloadResponse() {
    }

    public PayloadResponse(T payload, String status, String message) {
        this.payload = payload;
        this.status = status;
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}