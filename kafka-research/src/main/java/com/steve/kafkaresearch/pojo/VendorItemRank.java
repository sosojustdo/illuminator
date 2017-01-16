package com.steve.kafkaresearch.pojo;


import java.io.Serializable;

/**
 * Created by stevexu on 1/16/17.
 */
public class VendorItemRank implements Serializable {

    private Long vendorItemId;

    private String vendorId;

    private int rank;

    public VendorItemRank(Long vendorItemId, String vendorId, int rank) {
        this.vendorItemId = vendorItemId;
        this.vendorId = vendorId;
        this.rank = rank;
    }

    public VendorItemRank(){

    }

    public Long getVendorItemId() {
        return vendorItemId;
    }

    public void setVendorItemId(Long vendorItemId) {
        this.vendorItemId = vendorItemId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
