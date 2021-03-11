package com.alex.kumparaturi.service;

import com.alex.kumparaturi.jobs.JobInterface;
import com.alex.kumparaturi.model.JobsConfig;
import com.alex.kumparaturi.repository.JobsConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

//comment this for disable jobs
//@Profile("dev")
//@Service
public class JobSchedulerService implements SchedulingConfigurer {

    private static Logger logger = LoggerFactory.getLogger(JobSchedulerService.class);

    @Autowired
    JobsConfigRepository jobsConfigRepository;

    @Autowired
    List<JobInterface> allJobs;

//    @Autowired
//    DeleteExpiredTokenJob deleteExpiredTokenJob;

//    @Autowired
//    TestJob testJob;

    @Bean
    public TaskScheduler poolScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        scheduler.setPoolSize(1);
        scheduler.initialize();
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(poolScheduler());
        getJobList();
//        refreshJobList( poolScheduler());
    }

    public void getJobList() {
        List<JobsConfig>  jobsList = jobsConfigRepository.findAll();
        for (JobsConfig databaseJob : jobsList) {
            ScheduledFuture<?> newJob = null;
            for (JobInterface job : allJobs) {
                if (job.getJobName().equals(databaseJob.getJobName())) {
                    newJob = scheduleJob(poolScheduler(), job, databaseJob);
                    if (newJob == null) {
                        logger.info("New job not created !");
                    }
                }
            }
        }
    }

    public ScheduledFuture<?> scheduleJob(TaskScheduler scheduler, JobInterface jobInterface, JobsConfig job){
        return scheduler.schedule(new Runnable(){
            @Override
            public void run() {
                jobInterface.jobCode();
            }
        }, new Trigger(){
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                Optional <JobsConfig> refreshedJob = jobsConfigRepository.findByJobName(job.getJobName());
                String cronExp = refreshedJob.get().getScheduledValue();
                return new CronTrigger(cronExp).nextExecutionTime(triggerContext);
            }
        });
    }

//    public void getJobList() {
//        List<JobsConfig>  jobsList = jobsConfigRepository.findAll();
//        for (JobsConfig jobName : jobsList) {
//            switch (jobName.getJobName()) {
//                case "DeleteExpiredTokenJob":
//                    scheduleJob(poolScheduler(), deleteExpiredTokenJob, jobName.getJobName());
//                    break;
//                case "TestJob":
//                    scheduleJob(poolScheduler(), testJob, jobName.getJobName());
//                    break;
//                default:
//                    logger.info(String.format("JOB NOT FOUND [%s]", jobName.getJobName()));
//            }
//        }
//    }

//    private void refreshJobList(TaskScheduler scheduler){
//        scheduler.schedule(new Runnable(){
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName()+" The Task2 executed at "+ new Date());
//                getJobList();
//            }
//        }, new Trigger(){
//            @Override
//            public Date nextExecutionTime(TriggerContext triggerContext) {
//                String cronExp="0/10 * * * * ?";//Can be pulled from a db . This will run every minute
//                return new CronTrigger(cronExp).nextExecutionTime(triggerContext);
//            }
//        });
//    }



//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.setScheduler(poolScheduler());
//
////        CronTrigger croneTrigger = new CronTrigger("0/5 * * * * ?", TimeZone.getDefault());
////        taskRegistrar.addTriggerTask(() -> loadJobsConfig(taskRegistrar), croneTrigger);
//
//
//        List<JobsConfig> jobsList= jobsConfigRepository.findAll();
//        jobsList= jobsConfigRepository.findAll();
//        for(JobsConfig jobName : jobsList) {
//            try {
//                switch (jobName.getJobName()) {
//                    case "DeleteExpiredTokenJob":
////                        deleteExpiredTokenJob.jobCode();
////                        scheduleJob(deleteExpiredTokenJob, taskRegistrar, jobName.getScheduledValue());
//                        scheduleJob(deleteExpiredTokenJob, taskRegistrar, jobsConfigurationService.getJobsConfig("DeleteExpiredTokenJob").getScheduledValue());
//                        break;
//                    default:
//                        logger.info(String.format("JOB NOT FOUND [%s]", jobName.getJobName()));
//                }
//            } catch (Exception e) {
//                logger.info("JOB RUNNING ERROR: ", e.getMessage());
//            }
//        }
//    }
//
//    public void scheduleJob(JobInterface jobInterface, ScheduledTaskRegistrar taskRegistrar, String cronValue) {
//        CronTrigger croneTrigger = new CronTrigger(cronValue, TimeZone.getDefault());
//        taskRegistrar.addTriggerTask(() -> jobInterface.jobCode(), croneTrigger);
//    }
//
//    public void loadJobsConfig(ScheduledTaskRegistrar taskRegistrar) {
////        return jobsConfigRepository.findAll();
//        List<JobsConfig> jobsList= jobsConfigRepository.findAll();
////        jobsList= jobsConfigRepository.findAll();
//        for(JobsConfig jobName : jobsList) {
//            logger.info("Cron value: " + jobName.getScheduledValue());
//            try {
//                switch (jobName.getJobName()) {
//                    case "DeleteExpiredTokenJob":
////                        deleteExpiredTokenJob.jobCode();
////                        scheduleJob(deleteExpiredTokenJob, taskRegistrar, jobName.getScheduledValue());
//                        scheduleJob(deleteExpiredTokenJob, taskRegistrar, jobName.getScheduledValue());
//                        break;
//                    default:
//                        logger.info(String.format("JOB NOT FOUND [%s]", jobName.getJobName()));
//                }
//            } catch (Exception e) {
//                logger.info("JOB RUNNING ERROR: ", e.getMessage());
//            }
//        }
//    }


}
