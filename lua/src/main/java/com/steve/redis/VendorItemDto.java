package com.steve.redis;

/**
 * Created by stevexu on 12/23/16.
 */
public class VendorItemDTO {

    private String vendorItemId;

    private String vendorId;

    private boolean isCRV;

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

    public boolean isCRV() {
        return isCRV;
    }

    public void setCRV(boolean CRV) {
        isCRV = CRV;
    }

    public VendorItemDTO(String vendorItemId, String vendorId, boolean isCRV) {
        this.vendorItemId = vendorItemId;
        this.vendorId = vendorId;
        this.isCRV = isCRV;
    }
}
