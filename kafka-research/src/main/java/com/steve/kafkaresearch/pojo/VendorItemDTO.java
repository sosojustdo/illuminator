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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VendorItemDTO that = (VendorItemDTO) o;

        if (vendorItemId != null ? !vendorItemId.equals(that.vendorItemId) : that.vendorItemId != null) return false;
        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        return newSellerRate != null ? newSellerRate.equals(that.newSellerRate) : that.newSellerRate == null;

    }

    @Override
    public int hashCode() {
        int result = vendorItemId != null ? vendorItemId.hashCode() : 0;
        result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
        result = 31 * result + (newSellerRate != null ? newSellerRate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VendorItemDTO{" +
                "vendorItemId=" + vendorItemId +
                ", itemId=" + itemId +
                ", newSellerRate=" + newSellerRate +
                '}';
    }
}
