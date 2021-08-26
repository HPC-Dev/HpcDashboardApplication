package com.results.HpcDashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobDto implements Serializable {
    @JsonProperty("bmName")
    private String bmName;

    @JsonProperty("cpu")
    private String cpu;

    @JsonProperty("nodes")
    private Integer nodes;

    @JsonProperty("runType")
    private String runType;

    @JsonProperty("cores")
    private int cores;

    @JsonProperty("appName")
    private String appName;

    @JsonProperty("category")
    private String category;


    public JobDto(Object[] column) {
        this.bmName = (String) column[0];
        this.cpu = (String) column[1];
        this.nodes = (int) column[2];
        this.runType = (String) column[3];
        this.cores = (int) column[4];
        this.appName = (String) column[5];
        this.category = (String) column[6];
    }
}
