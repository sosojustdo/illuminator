package com.steve.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by stevexu on 12/28/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendorItemRecommendationDTO {

    @JsonProperty("vi")
    private String vendorItemId;

    @JsonProperty("r")
    private String r;

    @JsonProperty("v")
    private String vendorId;

    public String getVendorItemId() {
        return vendorItemId;
    }

    public void setVendorItemId(String vendorItemId) {
        this.vendorItemId = vendorItemId;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public VendorItemRecommendationDTO(String vi, String r, String v) {
        this.vendorItemId = vi;
        this.r = r;
        this.vendorId = v;
    }

    public VendorItemRecommendationDTO(){

    }
}
