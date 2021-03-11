package com.alex.kumparaturi.model;

import javax.persistence.*;

@Entity
@Table(name = "jobs_config")
public class JobsConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_name")
    private String jobName;

    private String scheduledValue;

    public JobsConfig() {
    }

    public JobsConfig(String jobName, String scheduledValue) {
        this.jobName = jobName;
        this.scheduledValue = scheduledValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getScheduledValue() {
        return scheduledValue;
    }

    public void setScheduledValue(String scheduledValue) {
        this.scheduledValue = scheduledValue;
    }
}
