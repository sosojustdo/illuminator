package com.steve.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author stevexu
 * @Since 9/30/17
 */
@Configuration
public class CassandraContextConfig {

    @Bean
    public Cluster CassandraCluster() {
        PoolingOptions poolingOptions = new PoolingOptions();
        poolingOptions
                .setCoreConnectionsPerHost(HostDistance.LOCAL, 4)
                .setMaxConnectionsPerHost(HostDistance.LOCAL, 8)
                .setCoreConnectionsPerHost(HostDistance.REMOTE, 4)
                .setMaxConnectionsPerHost(HostDistance.REMOTE, 8)
                .setMaxRequestsPerConnection(HostDistance.LOCAL, 8192)
                .setMaxRequestsPerConnection(HostDistance.REMOTE, 1024)
                .setHeartbeatIntervalSeconds(60)
                .setPoolTimeoutMillis(5000);


        Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042)
                                 .withPoolingOptions(poolingOptions)
                                 .build();
        return cluster;
    }




}
