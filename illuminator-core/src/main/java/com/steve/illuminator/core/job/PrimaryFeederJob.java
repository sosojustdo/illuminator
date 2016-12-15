package com.steve.illuminator.core.job;

import akka.actor.*;
import akka.japi.Function;
import akka.routing.FromConfig;
import akka.routing.RoundRobinRouter;
import com.steve.illuminator.core.excpetion.TimeOutException;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static akka.actor.SupervisorStrategy.*;

/**
 * Created by xuh18 on 7/5/16.
 */
public class PrimaryFeederJob extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(PrimaryFeederJob.class);

    private ActorRef workActor;
    private ActorRef aggregateSummaryActor;

    private int total = 20;

    private static SupervisorStrategy strategy = new OneForOneStrategy(10,
            Duration.create("10 second"), new Function<Throwable, SupervisorStrategy.Directive>() {
        public SupervisorStrategy.Directive apply(Throwable t) {
            if (t instanceof ArithmeticException) {
                return resume();
            } else if (t instanceof TimeOutException) {
                return restart();
            } else if (t instanceof IllegalArgumentException) {
                return stop();
            } else {
                return escalate();
            }
        }
    });

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            if(((String) message).equalsIgnoreCase("startWork")){
                List<JobMessage> jobMessageList = new ArrayList<>();
                for(int i=1;i<=total;i++){
                    if(i % 10 ==0){
                        JobMessage jobMessage = new JobMessage("Job"+i, "Job"+i, 1*1000L*60);
                        jobMessageList.add(jobMessage);
                    }
                    else{
                        JobMessage jobMessage = new JobMessage("Job"+i, "Job"+i, i*1000L);
                        jobMessageList.add(jobMessage);
                    }
                }
                aggregateSummaryActor = context().actorOf(Props.create(AggregateSummaryJob.class, total, this.getSelf()).withDispatcher("aggregate-dispatcher"), "aggregatejob");
                workActor = context().actorOf(FromConfig.getInstance().props(Props.create(ChildWorkJob.class, aggregateSummaryActor)).withDispatcher("aggregate-dispatcher"),"childworkjob");
                jobMessageList.stream().forEach(jobMessage->
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    workActor.tell(jobMessage, this.getSelf());
                });
            }
            else if(((String) message).equalsIgnoreCase("cancelJob")){
                logger.info("now cancel the job, stop the primary...");
                context().stop(this.getSelf());
                context().system().shutdown();
            }
            else{
                throw new IllegalArgumentException("Unknown message [" + message
                        + "]");
            }
        }

        else if(message instanceof AggregateSummaryJob.Summary){
            AggregateSummaryJob.Summary summary = (AggregateSummaryJob.Summary) message;
            System.out.println("Now print the summary");
            System.out.println("success count:"+summary.getSuccessCount());
            System.out.println("failed count:"+summary.getFailedCount());
            System.out.println("success list:"+summary.getSuccessedList().stream().collect(Collectors.joining(",")));
            System.out.println("failed list:" + summary.getFailedList().stream().collect(Collectors.joining(",")));
            System.out.println("timeout list:" + summary.getTimeoutCount());
            context().stop(this.getSelf());
            context().system().shutdown();
        }
        else
            throw new IllegalArgumentException("Unknown message [" + message
                    + "]");
    }


}
