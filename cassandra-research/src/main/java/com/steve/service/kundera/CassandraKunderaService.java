package com.steve.service.kundera;

import com.steve.entity.kundera.VendorItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author stevexu
 * @Since 9/29/17
 */
@Service
@Slf4j
public class CassandraKunderaService {

    @Inject
    private CassandraKunderaVendorItemService cassandraVendorItemService;

    public void testKunderaCassandra(int rotation){
        StopWatch watch = new StopWatch();
        watch.start();
        for(int i=0; i<=rotation; i++){
            VendorItem vendorItem = new VendorItem(3000000000L + i, 5000L, 10000L, "A00010028", new Date(), new Date(), false,
                                                   false, new BigDecimal(30000.00), false, null, false, new Date(), new Date(),
                                                   new Date(), false);
            cassandraVendorItemService.save(vendorItem);
            cassandraVendorItemService.findOne(3000000000L+i);
        }
        watch.stop();
        log.info("process vendor items take {} ms", watch.getTime());
    }

}
