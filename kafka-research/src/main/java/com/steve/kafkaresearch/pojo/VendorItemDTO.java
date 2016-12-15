package com.steve.kafkaresearch.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by stevexu on 12/12/16.
 */
public class VendorItemDTO implements Serializable{

    private Long vendorItemId;

    private Long itemId;

    private Double newSellerRate;

    public Long getVendorItemId() {
        return vendorItemId;
    }

    public void setVendorItemId(Long vendorItemId) {
        this.vendorItemId = vendorItemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Double getNewSellerRate() {
        return newSellerRate;
    }

    public void setNewSellerRate(Double newSellerRate) {
        this.newSellerRate = newSellerRate;
    }

    public VendorItemDTO(){

    }

    public VendorItemDTO(Long vendorItemId, Long itemId, Double newSellerRate) {
        this.vendorItemId = vendorItemId;
        this.itemId = itemId;
        this.newSellerRate = newSellerRate;
    }
}
