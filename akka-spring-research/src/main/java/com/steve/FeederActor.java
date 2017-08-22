package com.steve;

import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.util.List;

import static akka.actor.SupervisorStrategy.*;

/**
 * Created by stevexu on 5/2/17.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FeederActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(FeederActor.class);

    /*private static final SupervisorStrategy strategy = new OneForOneStrategy(
            DeciderBuilder //
                    .match(IllegalArgumentException.class, e -> SupervisorStrategy.resume())//
                    .match(ActorInitializationException.class, e -> SupervisorStrategy.stop())//
                    .match(Exception.class, e -> SupervisorStrategy.restart())//
                    .matchAny(o -> SupervisorStrategy.escalate()).build());*/

    /*@Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }*/

    private List<VendorItemMsg> vendorItemMsgList;

    public FeederActor(List<VendorItemMsg> vendorItemMsgList){
        this.vendorItemMsgList = vendorItemMsgList;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String && message.equals("startWork")){

        }
    }
}
