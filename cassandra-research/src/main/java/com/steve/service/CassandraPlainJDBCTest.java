package com.steve.service;

import com.steve.entity.plain.VendorItem;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author stevexu
 * @Since 9/25/17
 */
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

        }
        finally {
            cassandraPlainService.close();
        }
    }

}
