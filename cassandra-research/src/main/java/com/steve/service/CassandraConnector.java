package com.steve.service;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * @author stevexu
 * @Since 9/25/17
 */
public class CassandraConnector {

    private Cluster cluster;

    private Session session;

    public void connect(String node, Integer port) {
        Cluster.Builder b = Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();

        session = cluster.connect();
    }

    public Session getSession() {
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }
}

