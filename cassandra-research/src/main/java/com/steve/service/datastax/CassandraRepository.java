package com.steve.service.datastax;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * @author stevexu
 * @Since 9/30/17
 */
@Repository
@Slf4j
public class CassandraRepository {

    private Session session;

    @PostConstruct
    public void initSession(){
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
        if (session == null) {
            session = cluster.connect();
        }
    }

    public Session getSession(){
        return this.session;
    }

    @PreDestroy
    public void close() {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                log.error("Error while closing the Session ...");
            }
        }
    }

}
