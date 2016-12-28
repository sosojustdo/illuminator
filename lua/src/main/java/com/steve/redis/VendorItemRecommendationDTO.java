package com.steve.redis;

/**
 * Created by stevexu on 12/28/16.
 */
public class VendorItemRecommendationDTO {

    private String vendorItemId;

    private String reason;

    private String vendorId;

    public VendorItemRecommendationDTO(String vendorItemId, String reason, String vendorId) {
        this.vendorItemId = vendorItemId;
        this.reason = reason;
        this.vendorId = vendorId;
    }

    public String getVendorItemId() {
        return vendorItemId;
    }

    public void setVendorItemId(String vendorItemId) {
        this.vendorItemId = vendorItemId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }
}
