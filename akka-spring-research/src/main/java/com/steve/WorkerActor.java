package com.steve;

import akka.actor.UntypedActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by stevexu on 5/2/17.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkerActor extends UntypedActor {

    @Autowired
    VendorItemService vendorItemService;

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof VendorItemMsg){
            vendorItemService.processVendorItem((VendorItemMsg)message);
        }

    }
}
