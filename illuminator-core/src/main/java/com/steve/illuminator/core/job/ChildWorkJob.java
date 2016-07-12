package com.steve.illuminator.core.job;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by xuh18 on 7/5/16.
 */
public class ChildWorkJob extends UntypedActor {

    private ActorRef aggregate;

    private static final Logger logger = LoggerFactory.getLogger(ChildWorkJob.class);

    public ChildWorkJob(ActorRef aggregate){
         this.aggregate = aggregate;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof JobMessage){
            JobMessage jobMessage = (JobMessage) message;
            Thread.sleep(jobMessage.getDuration());
            logger.info(jobMessage.getTaskMessage()+" has finished.");
            JobResult result = new JobResult(jobMessage.getId(),true);
            aggregate.tell(result,this.getSender());
        }
        else
            throw new IllegalArgumentException("Unknown message [" + message
                    + "]");
    }

    @Override
    public void postStop(){
         logger.info("get stop singnal, worker will stop");
    }
}
