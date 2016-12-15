package com.steve.illuminator.core.scheduler;

import com.steve.illuminator.core.reg.JobConfiguration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

/**
 * Created by xuh18 on 7/1/16.
 */
public class SchedulerService {

    public static void main(String args[]) throws SchedulerException, InterruptedException {
        String cronExpression = "*/10 * * * * ?";
        // Create a Scheduler and schedule the Job
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        // Create a JobDetail for the Job
        JobDetail jobDetail = JobBuilder.newJob(DateFetchSchedulerJob.class).withIdentity("DataFetchJob","group1").build();
        JobConfiguration jobConfiguration = new JobConfiguration();
        jobConfiguration.setNodeId("dummynode");
        jobConfiguration.setJobExpression(cronExpression);
        // Configure the directory to scan
        jobDetail.getJobDataMap().put("jobconfig",jobConfiguration); //set the JobDataMap that is associated with the Job.
        // Create a trigger that fires every 10 seconds, forever
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1").withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        Thread.sleep(1000*20*1);
        List<JobExecutionContext> currentlyExecuting = scheduler.getCurrentlyExecutingJobs();

        for (JobExecutionContext jobExecutionContext : currentlyExecuting) {
            if(jobExecutionContext.getJobDetail().getKey().getName().equals("DataFetchJob")){
                scheduler.interrupt(jobExecutionContext.getJobDetail().getKey());
            }
        }
    }

}
