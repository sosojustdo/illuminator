package com.steve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by stevexu on 5/2/17.
 */
@Component
public class VendorItemService {

    private static final Logger logger = LoggerFactory.getLogger(VendorItemService.class);

    public boolean processVendorItem(VendorItemMsg vendorItemMsg){
        logger.info("Process vendor item message:"+vendorItemMsg);
        return true;
    }

}
