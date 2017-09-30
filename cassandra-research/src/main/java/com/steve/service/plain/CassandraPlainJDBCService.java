package com.steve.service.plain;

import com.datastax.driver.core.*;
import com.steve.entity.plain.PlainVendor;
import com.steve.entity.plain.PlainVendorItem;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author stevexu
 * @Since 9/25/17
 */
@Slf4j
@Service
public class CassandraPlainJDBCService {

    private Cluster cluster;

    private Session session;

    private PreparedStatement saveViStmt;

    private PreparedStatement queryViStmt;

    private PreparedStatement queryVendorStmt;

    @PostConstruct
    private void initStatements(){
        Cluster.Builder b = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042);
        cluster = b.build();
        session = cluster.connect();
        StringBuilder insertStr = new StringBuilder("update buyboxtest.vendor_items set ")
                .append("itemid=?, vendorid=?, banned=?, deleted=?, startedat=?, endedat=?, productid=?,")
                .append("soldout=?, rates=?, used=?, message_data=?, message_eventtime=?, message_obsolete=?,")
                .append("createdat=?, modifiedat=? where vendoritemid= ?");
        saveViStmt = session.prepare(insertStr.toString());

        StringBuilder queryStr = new StringBuilder("select * from buyboxtest.vendor_items where vendoritemid = ?");
        queryViStmt = session.prepare(queryStr.toString());

        StringBuilder queryVendorStr = new StringBuilder("select * from buyboxtest.vendors where vendorid = ?");
        queryVendorStmt = session.prepare(queryVendorStr.toString());
    }

    public void testPlainCassandra(int rotation){
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            for(int i=0; i<=rotation; i++){
                /*PlainVendorItem vendorItem = new PlainVendorItem(3000000000L+i, 10000L, false, false, null, new Date(),
                                                                 new Date(), new Date(), false, 5000L, new BigDecimal(20000.00), false, false,
                                                                 "A00010028", new Date(), new Date());
                saveVendorItems(vendorItem);*/
                queryVendorItem(3000000000L+i);
            }
            watch.stop();
            log.info("process vendor items take {} ms", watch.getTime());
        }catch(Exception ex){
            log.error("save failed", ex);
        }
        finally {
            close();
        }
    }

    public void close() {
        session.close();
        cluster.close();
    }

    public void saveVendorItems(PlainVendorItem vendorItem) {
        session.execute(saveViStmt.bind(vendorItem.getItemid(), vendorItem.getVendorId(),
                                               vendorItem.isBanned(), vendorItem.isDeleted(), vendorItem.getStartedAt(),
                                               vendorItem.getEndedAt(), vendorItem.getProductId(), vendorItem.isSoldOut(),
                                               vendorItem.getRates(), vendorItem.isUsed(), vendorItem.getMessage_data(),
                                               vendorItem.getMessageEventTime(), vendorItem.isMessageObsolete(),
                                               vendorItem.getCreatedAt(), vendorItem.getModifiedAt(), vendorItem.getVendoritemid()).setConsistencyLevel(ConsistencyLevel.QUORUM));
    }

    public PlainVendorItem queryVendorItem(long vendorItemId) {
        ResultSet rs = session.execute(queryViStmt.bind(vendorItemId).setConsistencyLevel(ConsistencyLevel.QUORUM));
        if (!rs.isExhausted()) {
            Row row = rs.one();
            PlainVendorItem vendorItem = new PlainVendorItem(row.getLong("vendoritemid"), row.getLong("itemid"),
                                                             row.getBool("banned"), row.getBool("deleted"),
                                                             row.getString("message_data"), row.getTimestamp("startedat"),
                                                             row.getTimestamp("endedat"), row.getTimestamp("message_eventtime"),
                                                             row.getBool("message_obsolete"), row.getLong("productId"),
                                                             row.getDecimal("rates"), row.getBool("soldout"), row.getBool("used"),
                                                             row.getString("vendorid"), row.getTimestamp("createdat"), row.getTimestamp("modifiedat"));
            return vendorItem;
        }
        return null;
    }

    public PlainVendor queryVendor(String vendorId) {
        ResultSet rs = session.execute(queryVendorStmt.bind(vendorId).setConsistencyLevel(ConsistencyLevel.QUORUM));
        if (!rs.isExhausted()) {
            Row row = rs.one();
            PlainVendor vendor = new PlainVendor(row.getString("vendorid"), row.getBool("banned"),
                                                 row.getInt("d1"), row.getInt("d2"),
                                                 row.getInt("d3"), row.getInt("d4"),
                                                 row.getInt("d5"), row.getInt("d6"),
                                                 row.getInt("d7"), row.getInt("d8"),
                                                 row.getString("holidayflag"), row.getDecimal("orderfulfillmentrate"),
                                                 row.getDecimal("pricingdiscount"), row.getDecimal("rate"),
                                                 row.getLong("totalorders"));
            return vendor;
        }
        return null;
    }



}
