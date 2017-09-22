package com.steve.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by stevexu on 12/23/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendorItemDTO {

    @JsonProperty("vi")
    private String vendorItemId;

    @JsonProperty("v")
    private String vendorId;

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

    public VendorItemDTO(String vi, String v) {
        this.vendorItemId = vi;
        this.vendorId = v;
    }

    public VendorItemDTO(){

    }

}
