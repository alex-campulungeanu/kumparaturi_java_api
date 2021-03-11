package com.alex.kumparaturi.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
public class TestJob implements JobInterface {
    @Value("${app.databaseSchema}")
    private String schema2;

    private String schema = "kumparaturi";

    @Autowired
    EntityManager entityManager;

    public static final Logger logger = LoggerFactory.getLogger(TestJob.class);

    public String getJobName() {
        return this.getClass().getSimpleName();
    }

    public void jobCode() {
        logger.info("START TestJob !");
    }
}
