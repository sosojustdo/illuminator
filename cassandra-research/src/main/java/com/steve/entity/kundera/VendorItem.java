package com.steve.entity.kundera;

/**
 * @author stevexu
 * @Since 9/27/17
 */

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@NamedQueries(
        {@NamedQuery(
                name = QueryHelper.NamedQueries.CASSANDRA_FIND_VENDOR_ITEM_BY_VENDOR_ITEM_ID,
                query = "select cvi from VendorItem cvi where cvi.vendorItemId = :vendorItemId"),
                @NamedQuery(
                        name = QueryHelper.NamedQueries.CASSANDRA_FIND_VENDOR_ITEMS_BY_VENDOR_ID,
                        query = "select cvi from VendorItem cvi where cvi.vendorId = :vendorId"),
                @NamedQuery(
                        name = QueryHelper.NamedQueries.CASSANDRA_FIND_VENDOR_ITEMS_STARTED_IN_PERIOD,
                        query = "select cvi from VendorItem cvi where cvi.startedAt >= :periodStart and cvi.startedAt < :periodEnd"),
                @NamedQuery(
                        name = QueryHelper.NamedQueries.CASSANDRA_FIND_VENDOR_ITEMS_ENDED_IN_PERIOD,
                        query = "select cvi from VendorItem cvi where cvi.endedAt >= :periodStart and cvi.endedAt < :periodEnd")
        })
@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
@IndexCollection(columns = {
        @Index(name = "vendorId"),
        @Index(name = "startedAt"),
        @Index(name = "endedAt")
})
@Table(name = "vendor_items")
public class VendorItem implements Serializable {
    // vendor items
    @Id
    @Column(name = "vendoritemid")
    private long vendorItemId;

    @Column(name = "productid")
    private long productId;

    @Column(name = "itemid")
    private long itemId;

    @Column(name = "vendorid")
    private String vendorId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "startedat")
    private Date startedAt;

    @Temporal(TemporalType.TIMESTAMP)
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdat")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedat")
    private Date modifiedAt;

    @Column(name = "banned")
    private boolean banned;

}

