package com.steve;

/**
 * @author stevexu
 * @Since 9/29/17
 */

import com.steve.entity.cassandra.VendorItem;
import com.steve.service.CassandraVendorItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;

@EnableAutoConfiguration
@SpringBootApplication
@Slf4j
@ComponentScan({"com.steve.service","com.steve.config"})
public class CassandraApplication implements CommandLineRunner {

    @Inject
    private CassandraVendorItemService cassandraVendorItemService;

    public static void main(String[] args) {
        SpringApplication.run(CassandraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        VendorItem vendorItem = new VendorItem(3000000002L, 5000L, 10000L, "A00010028", new Date(), new Date(), false,
                                               false, new BigDecimal(30000.00), false, null, false, new Date(), new Date(),
                                               new Date(), false);
        cassandraVendorItemService.save(vendorItem);
        log.info("vi:"+cassandraVendorItemService.findOne(3000000001L));
    }
}

