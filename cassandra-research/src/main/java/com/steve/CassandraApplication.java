package com.steve;

/**
 * @author stevexu
 * @Since 9/29/17
 */

import com.steve.service.datastax.CassandraDictService;
import com.steve.service.datastax.CassandraVendorItemService;
import com.steve.service.jpa.JPAVendorItemService;
import com.steve.service.kundera.CassandraKunderaService;
import com.steve.service.plain.CassandraPlainJDBCService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import javax.inject.Inject;

@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@SpringBootApplication
@Slf4j
@ComponentScan({"com.steve.service","com.steve.config","com.steve.controller","com.steve.repository"})
@PropertySource({"classpath:application.properties"})
public class CassandraApplication implements CommandLineRunner {

    @Inject
    private CassandraPlainJDBCService cassandraPlainJDBCService;

    @Inject
    private CassandraKunderaService cassandraKunderaService;

    @Inject
    private CassandraVendorItemService cassandraVendorItemService;

    @Inject
    private JPAVendorItemService jpaVendorItemService;

    @Inject
    private CassandraDictService cassandraDictService;

    public static void main(String[] args) {
        SpringApplication.run(CassandraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //cassandraKunderaService.testKunderaCassandra(300000);
        //cassandraPlainJDBCService.testPlainCassandra(300000);
        //cassandraVendorItemService.testDatastaxCassandra(1000000);
        //jpaVendorItemService.testJPAVendorItem(300000);
        cassandraDictService.process();
    }
}

