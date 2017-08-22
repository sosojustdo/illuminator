package com.steve;

/**
 * Created by stevexu on 5/2/17.
 */
public class VendorItemMsg {

    private Long vendorItemId;

    private Long itemId;

    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "VendorItemMsg{" +
                "vendorItemId=" + vendorItemId +
                ", itemId=" + itemId +
                ", content='" + content + '\'' +
                '}';
    }
}
