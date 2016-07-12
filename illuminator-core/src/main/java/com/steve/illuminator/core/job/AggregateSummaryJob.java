package com.steve.illuminator.core.job;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import akka.actor.ReceiveTimeout;
import scala.concurrent.duration.Duration;

/**
 * Created by xuh18 on 7/5/16.
 */
public class AggregateSummaryJob extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(AggregateSummaryJob.class);

    private final int sendCount;

    private ActorRef primaryActor;

    private int successCount = 0;

    private int failedCount = 0;

    private int timeoutCount = 0;

    private List<String> failedList = new ArrayList<>();

    private List<String> successedList = new ArrayList<>();

    public AggregateSummaryJob(int sendCount, ActorRef primaryActor){
        this.sendCount = sendCount;
        this.primaryActor = primaryActor;
        context().setReceiveTimeout(Duration.create(20, TimeUnit.SECONDS));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof JobResult){
            JobResult jobResult = (JobResult) message;
            if(jobResult.isSuccess()){
                successCount++;
                successedList.add(jobResult.getMessageId());
            }
            else{
                failedCount++;
                successedList.add(jobResult.getMessageId());
            }
            if(successCount+failedCount>=sendCount){
                primaryActor.tell(new Summary(successCount,failedCount,successedList,failedList,timeoutCount),this.getSelf());
                context().stop(this.getSelf());
            }
        }
        else if(message instanceof ReceiveTimeout){
            timeoutCount++;
            logger.info(" get time out ");
        }
        else
            throw new IllegalArgumentException("Unknown message [" + message
                    + "]");

    }

    @Override
    public void postStop(){
        logger.info("get stop singnal, aggregate will stop");
    }

    public static class Summary {

        private final int successCount;

        private final int failedCount;

        private final int timeoutCount;

        private final List<String> failedList;

        private final List<String> successedList;

        public Summary(int successCount, int failedCount, List<String> successedList, List<String> failedList, int timeoutCount) {
            this.successCount = successCount;
            this.failedCount = failedCount;
            this.successedList = successedList;
            this.failedList = failedList;
            this.timeoutCount = timeoutCount;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getFailedCount() {
            return failedCount;
        }

        public List<String> getFailedList() {
            return failedList;
        }

        public List<String> getSuccessedList() {
            return successedList;
        }

        public int getTimeoutCount() {
            return timeoutCount;
        }

    }
}
