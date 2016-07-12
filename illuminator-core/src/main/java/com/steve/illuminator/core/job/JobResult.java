package com.steve.illuminator.core.job;

/**
 * Created by xuh18 on 7/7/16.
 */
public class JobResult {

    private final String messageId;

    private final boolean isSuccess;

    public JobResult(String messageId, boolean isSuccess) {
        this.messageId = messageId;
        this.isSuccess = isSuccess;
    }

    public String getMessageId() {
        return messageId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
