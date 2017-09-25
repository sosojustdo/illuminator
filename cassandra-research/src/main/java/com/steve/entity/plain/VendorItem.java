package com.steve.entity.plain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author stevexu
 * @Since 9/25/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorItem implements Serializable{

    private long vendoritemid;

    private long itemid;

    private boolean banned;

    private boolean deleted;

    private String message_data;

    private Date startedAt;

    private Date endedAt;

    private Date messageEventTime;

    private boolean messageObsolete;

    private long productId;

    private BigDecimal rates;

    private boolean soldOut;

    private boolean used;

    private String vendorId;

    private Date createdAt;

    private Date modifiedAt;

}
