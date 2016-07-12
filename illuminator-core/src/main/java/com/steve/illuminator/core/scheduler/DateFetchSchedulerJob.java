package com.steve.illuminator.core.scheduler;

import com.steve.illuminator.core.reg.JobConfiguration;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by xuh18 on 7/1/16.
 */
public class DateFetchSchedulerJob implements InterruptableJob {

    private static final Logger logger = LoggerFactory.getLogger(DateFetchSchedulerJob.class);

    private boolean interrupted = false;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //Every job has its own job detail
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        // The directory to scan is stored in the job map
        JobDataMap dataMap = jobDetail.getJobDataMap();//任务所配置的数据映射表
        JobConfiguration jobConfiguration = (JobConfiguration) dataMap.get("jobconfig");
        // Log the time the job started
        logger.info(jobConfiguration.getNodeId()+" fired at " + new Date());//记录任务开始执行的时间
        while(true){
            if(interrupted){
                logger.info("job get finished");
                return;
            }
            else{
                try {
                    Thread.sleep(1000*5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        interrupted = true;
    }
}
