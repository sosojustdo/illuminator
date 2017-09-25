package com.steve.service;

import com.steve.entity.plain.VendorItem;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author stevexu
 * @Since 9/25/17
 */
@Slf4j
public class CassandraPlainJDBCTest {

    public static void main(String args[]){
        testPlainCassandra();
    }

    public static void testPlainCassandra(){
        CassandraPlainService cassandraPlainService = new CassandraPlainService();
        cassandraPlainService.connect();
        VendorItem vendorItem = new VendorItem(3000000001L, 10000L, false, false, null, new Date(),
                                               new Date(), new Date(), false, 5000L, new BigDecimal(20000.00), false, false,
                                               "A00010028", new Date(), new Date());
        try {
            cassandraPlainService.saveVendorItems(vendorItem);
            System.out.println(cassandraPlainService.queryVendorItem(3000000001L));
        }catch(Exception ex){
            log.error("save failed", ex);
        }
        finally {
            cassandraPlainService.close();
        }
    }

}
