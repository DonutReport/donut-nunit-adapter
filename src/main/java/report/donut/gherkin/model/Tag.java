package report.donut.gherkin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("line")
    @Expose
    private Integer line;
    @SerializedName("name")
    @Expose
    private String name;

    public Tag(String name, int line) {
        this.name = name;
        this.line = line;
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

}
