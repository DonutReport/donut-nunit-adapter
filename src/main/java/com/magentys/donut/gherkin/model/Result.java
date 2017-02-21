
package com.magentys.donut.gherkin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("duration")
    @Expose
    private Long duration;
    @SerializedName("status")
    @Expose
    private String status;

    public Result(String status, long duration) {
        this.status = status;
        this.duration = duration;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
