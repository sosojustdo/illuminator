package com.steve.service;

import com.datastax.driver.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.steve.entity.plain.Vendor;
import com.steve.entity.plain.VendorItem;

/**
 * @author stevexu
 * @Since 9/25/17
 */
public class CassandraPlainService {

    private Session session;

    private ObjectMapper objectMapper;

    CassandraConnector client = new CassandraConnector();

    public void connect() {
        client.connect("127.0.0.1", 9042);
        this.session = client.getSession();
    }

    public void saveVendorItems(VendorItem vendorItem) {
        StringBuilder insertStr = new StringBuilder("update buyboxtest.vendor_items set ")
                .append("itemid=?, vendorid=?, banned=?, deleted=?, startedat=?, endedat=?, productid=?,")
                .append("soldout=?, rates=?, used=?, message_data=?, message_eventtime=?, message_obsolete=?,")
                .append("createdat=?, modifiedat=? where vendoritemid= ?");
        PreparedStatement preparedStatement = session.prepare(insertStr.toString());
        session.execute(preparedStatement.bind(vendorItem.getItemid(), vendorItem.getVendorId(),
                                               vendorItem.isBanned(), vendorItem.isDeleted(), vendorItem.getStartedAt(),
                                               vendorItem.getEndedAt(), vendorItem.getProductId(), vendorItem.isSoldOut(),
                                               vendorItem.getRates(), vendorItem.isUsed(), vendorItem.getMessage_data(),
                                               vendorItem.getMessageEventTime(), vendorItem.isMessageObsolete(),
                                               vendorItem.getCreatedAt(), vendorItem.getModifiedAt(), vendorItem.getVendoritemid()).setConsistencyLevel(ConsistencyLevel.QUORUM));
    }

    public VendorItem queryVendorItem(long vendorItemId) {
        StringBuilder queryStr = new StringBuilder("select * from buyboxtest.vendor_items where vendoritemid = ?");
        PreparedStatement preparedStatement = session.prepare(queryStr.toString());
        ResultSet rs = session.execute(preparedStatement.bind(vendorItemId).setConsistencyLevel(ConsistencyLevel.QUORUM));
        if (!rs.isExhausted()) {
            Row row = rs.one();
            VendorItem vendorItem = new VendorItem(row.getLong("vendoritemid"), row.getLong("itemid"),
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

    public Vendor queryVendor(String vendorId) {
        StringBuilder queryStr = new StringBuilder("select * from buyboxtest.vendors where vendorid = ?");
        PreparedStatement preparedStatement = session.prepare(queryStr.toString());
        ResultSet rs = session.execute(preparedStatement.bind(vendorId).setConsistencyLevel(ConsistencyLevel.QUORUM));
        if (!rs.isExhausted()) {
            Row row = rs.one();
            Vendor vendor = new Vendor(row.getString("vendorid"),row.getBool("banned"),
                                       row.getInt("d1"),row.getInt("d2"),
                                       row.getInt("d3"),row.getInt("d4"),
                                       row.getInt("d5"),row.getInt("d6"),
                                       row.getInt("d7"),row.getInt("d8"),
                                       row.getString("holidayflag"),row.getDecimal("orderfulfillmentrate"),
                                       row.getDecimal("pricingdiscount"), row.getDecimal("rate"),
                                       row.getLong("totalorders"));
            return vendor;
        }
        return null;
    }

    public void close(){
        client.close();
    }

}
