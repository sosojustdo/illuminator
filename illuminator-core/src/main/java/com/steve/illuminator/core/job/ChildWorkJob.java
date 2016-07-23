package com.steve.illuminator.core.job;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.steve.illuminator.core.excpetion.TimeOutException;
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

    private int state = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof JobMessage){
            JobMessage jobMessage = (JobMessage) message;
            Thread.sleep(jobMessage.getDuration());
            if(jobMessage.getId().equals("Job5")){
                state++;
                logger.info("current state is "+state);
                if(state==1){
                    logger.info("state is 1, need throw timeout issue...");
                    throw new TimeOutException();
                }
            }
            /*if(jobMessage.getId().equals("Job10")){
                    throw new ArithmeticException("10/0");
            }
            if(jobMessage.getId().equals("Job15")){
                throw new IllegalArgumentException("stop illegal exception");
            }
            if(jobMessage.getId().equals("Job20")){
                throw new NullPointerException("throw null");
            }*/
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
         logger.info("get stop signal, worker will stop");
    }
}
