package com.steve.illuminator.core.job;

import akka.actor.*;
import akka.routing.RoundRobinRouter;
import com.typesafe.config.ConfigFactory;

/**
 * Created by xuh18 on 7/6/16.
 */
public class JobMain {

    public static void main(String args[]){
        ActorSystem system = ActorSystem.create("Illuminator", ConfigFactory.load().getConfig("IlluminatorConfig"));

        /*ActorRef aggreGateJob = system.actorOf(Props.create(AggregateSummaryJob.class),"aggregatejob");*/

        /*ActorRef workJob = system.actorOf(Props.create(ChildWorkJob.class,aggreGateJob),"primaryjob");*/

        ActorRef primaryJob = system.actorOf(Props.create(PrimaryFeederJob.class),"primaryjob");

        primaryJob.tell("startWork",ActorRef.noSender());

       /* // create the list of reduce Actors
        reduceRouter = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new ReduceActor(aggregateActor);
            }
        }).withRouter(new RoundRobinRouter(no_of_reduce_workers)));*/
    }

}
