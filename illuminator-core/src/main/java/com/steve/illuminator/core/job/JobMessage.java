package com.steve.illuminator.core.job;

import java.io.Serializable;

/**
 * Created by xuh18 on 7/6/16.
 */
public final class JobMessage implements Serializable {

    private final String id;

    private final String taskMessage;

    private final long duration;

    public JobMessage(String id, String taskMessage, long duration){
        this.id = id;
        this.taskMessage = taskMessage;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getTaskMessage() {
        return taskMessage;
    }

    public long getDuration() {
        return duration;
    }

}
