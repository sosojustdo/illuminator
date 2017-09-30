package com.steve.entity.datastax;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author stevexu
 * @Since 9/30/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(keyspace = "buyboxtest", name = "vendor_items",
       readConsistency = "QUORUM",
       writeConsistency = "QUORUM"
)
public class VendorItem implements Serializable{

    @PartitionKey
    @Column(name = "vendoritemid")
    private long vendoritemid;

    @Column(name = "productid")
    private long productId;

    @Column(name = "itemid")
    private long itemId;

    @Column(name = "vendorid")
    private String vendorId;

    @Column(name = "startedat")
    private Date startedAt;

    @Column(name = "endedat")
    private Date endedAt;

    @Column(name = "soldout")
    private boolean soldOut;

    @Column(name = "used")
    private boolean used;

    @Column(name = "rates")
    private BigDecimal rates;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "message_data")
    private String message_data;

    @Column(name = "message_obsolete")
    private boolean message_obsolete;

    @Column(name = "message_eventtime")
    private Date message_eventtime;

    @Column(name = "createdat")
    private Date createdAt;

    @Column(name = "modifiedat")
    private Date modifiedAt;

    @Column(name = "banned")
    private boolean banned;


}
