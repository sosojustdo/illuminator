package com.steve.entity.plain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author stevexu
 * @Since 9/25/17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlainVendor implements Serializable {

    private String vendorId;

    private boolean banned;

    private int d1;

    private int d2;

    private int d3;

    private int d4;

    private int d5;

    private int d6;

    private int d7;

    private int d8;

    private String holidayFlag;

    private BigDecimal orderfulfillmentrate;

    private BigDecimal pricingdiscount;

    private BigDecimal rate;

    private long totalorders;

}
