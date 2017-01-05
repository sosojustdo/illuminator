package com.steve.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by stevexu on 12/23/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendorItemDTO {

    @JsonProperty("vi")
    private String vendorItemId;

    @JsonProperty("v")
    private String vendorId;

    @JsonProperty("crv")
    private Boolean consignmentRetailVendor;

    public String getVendorItemId() {
        return vendorItemId;
    }

    public void setVendorItemId(String vendorItemId) {
        this.vendorItemId = vendorItemId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Boolean getCrv() {
        return consignmentRetailVendor;
    }

    public void setCrv(Boolean crv) {
        this.consignmentRetailVendor = crv;
    }

    public VendorItemDTO(String vi, String v) {
        this.vendorItemId = vi;
        this.vendorId = v;
    }

    public VendorItemDTO(){

    }

    public VendorItemDTO(String vi, String v, Boolean crv) {
        this.vendorItemId = vi;
        this.vendorId = v;
        this.consignmentRetailVendor = crv;
    }
}
