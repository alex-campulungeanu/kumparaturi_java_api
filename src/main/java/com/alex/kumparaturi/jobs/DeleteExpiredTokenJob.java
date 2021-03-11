package com.alex.kumparaturi.jobs;

import com.alex.kumparaturi.repository.AppLogsRepository;
import com.alex.kumparaturi.service.AppLogsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
public class DeleteExpiredTokenJob implements JobInterface {
    @Autowired
    EntityManager entityManager;

    @Autowired
    AppLogsService appLogsService;

    public static final Logger logger = LoggerFactory.getLogger(DeleteExpiredTokenJob.class);

    public String getJobName() {
        return this.getClass().getSimpleName();
    }

//    insert into kumparaturi.jobs_config(job_name, scheduled_value) values('DeleteExpiredTokenJob', '0 0 12 ? * MON *');
    @Transactional
    public void jobCode() {
        logger.info("START DeleteExpiredTokenJob !");
        appLogsService.saveLogToDatabase("START: " + getJobName());
        String removeSql = String.format("delete from {h-schema}password_reset_token where expiry_date < now()");
        entityManager.createNativeQuery(removeSql).executeUpdate();

    }
}
