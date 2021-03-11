package com.alex.kumparaturi.jobs;

import com.alex.kumparaturi.service.AppLogsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Component
public class PurgeAppLogsTableJob implements JobInterface{
    public static final Logger logger = LoggerFactory.getLogger(PurgeAppLogsTableJob.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    AppLogsService appLogsService;

    @Override
    public String getJobName() {
        return this.getClass().getSimpleName();
    }

//    insert into kumparaturi.jobs_config(job_name, scheduled_value) values('PurgeAppLogsTableJob', '0 0 12 ? * MON *');
    @Override
    @Transactional
    public void jobCode() {
        logger.info("START: " + getJobName());
        appLogsService.saveLogToDatabase("START: " + getJobName());
        String removeSql = String.format("delete from {h-schema}app_logs where create_Date < NOW() - INTERVAL '1 days'");
        entityManager.createNativeQuery(removeSql).executeUpdate();
    }

}
