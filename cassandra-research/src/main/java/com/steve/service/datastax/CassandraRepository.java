package com.steve.service.datastax;

import com.datastax.driver.core.Cluster;
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

    @Inject
    @Qualifier("cassandraCluster")
    Cluster cluster;

    private Session session;

    @PostConstruct
    public void initSession(){
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
