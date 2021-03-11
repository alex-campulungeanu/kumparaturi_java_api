package com.alex.kumparaturi.model;

import com.alex.kumparaturi.model.audit.DateAudit;
import com.alex.kumparaturi.model.audit.DateAuditCreate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity(name = "app_logs")
public class AppLogs extends DateAuditCreate<Instant> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    public AppLogs() {
    }

    public AppLogs(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
