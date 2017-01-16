package com.steve.kafkaresearch.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stevexu on 1/16/17.
 */
public class WinnerRankingDto implements Serializable {

    private Long itemId;

    private String channel;

    private Long eventTime;

    private List<VendorItemRank> vendorItemIdsByRank;

    public WinnerRankingDto(Long itemId, String channel, Long eventTime, List<VendorItemRank> vendorItemIdsByRank) {
        this.itemId = itemId;
        this.channel = channel;
        this.eventTime = eventTime;
        this.vendorItemIdsByRank = vendorItemIdsByRank;
    }

    public WinnerRankingDto(){

    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public List<VendorItemRank> getVendorItemIdsByRank() {
        return vendorItemIdsByRank;
    }

    public void setVendorItemIdsByRank(List<VendorItemRank> vendorItemIdsByRank) {
        this.vendorItemIdsByRank = vendorItemIdsByRank;
    }
}
