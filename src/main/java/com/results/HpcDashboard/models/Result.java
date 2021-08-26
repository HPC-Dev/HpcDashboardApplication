package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.*;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "results")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Builder
public class Result implements Serializable {

    @JsonAlias({"Job Id","Job_Id", "jobId"})
    @CsvBindByPosition(position = 0)
    @Id
    private String jobId;

    @JsonAlias({"App Name", "appName","App_Name"})
    @CsvBindByPosition(position = 1)
    private String appName;

    @JsonAlias({"Benchmark", "bmName"})
    @CsvBindByPosition(position = 2)
    private String bmName;

    @JsonAlias({"Nodes","nodes"})
    @CsvBindByPosition(position = 3)
    private int nodes;

    @JsonAlias({"Cores", "cores"})
    @CsvBindByPosition(position = 4)
    private int cores;

    @JsonAlias({"Node Name", "Node_Name", "nodeName"})
    @CsvBindByPosition(position = 5)
    private String nodeName;

    @JsonAlias({"Result", "result"})
    @CsvBindByPosition(position = 6)
    private double result;

    @JsonAlias({"CPU", "cpu"})
    @CsvBindByPosition(position = 7)
    private String cpu;

    @JsonAlias({"OS", "os"})
    @CsvBindByPosition(position = 8)
    private String os;

    @JsonAlias({"BIOS version", "Bios", "bios version" , "BIOS Version","BIOS"})
    @CsvBindByPosition(position = 9)
    private String biosVer;

    @JsonAlias({"Cluster","cluster"})
    @CsvBindByPosition(position = 10)
    private String cluster;

    @JsonAlias({"user", "User"})
    @CsvBindByPosition(position = 11)
    private String user;

    @JsonAlias({"platform", "Platform"})
    @CsvBindByPosition(position = 12)
    private String platform;

    @JsonAlias({"cpuGen", "CPU Generation", "CPU generation"})
    @CsvBindByPosition(position = 13)
    private String cpuGen;

    @JsonAlias({"runType", "Run Type", "Run type"})
    @CsvBindByPosition(position = 14)
    private String runType;

    @JsonAlias({"Setting", "setting"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String setting;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    //@CsvBindByPosition(position = 15)
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;

    @JsonAlias({"category"})
    @CsvBindByPosition(position = 15)
    String category;


    @JsonAlias({"TimeStamp", "Time Stamp"})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String time;
}
