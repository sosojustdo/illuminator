package com.steve.service;

import com.steve.entity.cassandra.VendorItem;
import com.steve.entity.plain.PlainVendorItem;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author stevexu
 * @Since 9/27/17
 */
@Slf4j
public class CassandraKunderaTest {

    public static void main(String args[]){
        testKunderaCassandra();
    }

    public static void testKunderaCassandra(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("buybox_cassandra");
        EntityManager em = entityManagerFactory.createEntityManager();

        CassandraVendorItemService cassandraVendorItemService = new CassandraVendorItemService();
        /*VendorItem vendorItem = new VendorItem(3000000002L, 5000L, 10000L, "A00010028",new Date(), new Date(), false,
                                               false, new BigDecimal(30000.00), false, null, false, new Date(), new Date(),
                                               new Date());*/
/*
        try {
            cassandraVendorItemService.save(vendorItem);
            System.out.println(cassandraVendorItemService.findOne(3000000002L));
        }catch(Exception ex){
            log.error("save failed", ex);
        }*/
    }

}
