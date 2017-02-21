
package com.magentys.donut.gherkin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("line")
    @Expose
    private Integer line;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("match")
    @Expose
    private Match match;
    @SerializedName("keyword")
    @Expose
    private String keyword;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
