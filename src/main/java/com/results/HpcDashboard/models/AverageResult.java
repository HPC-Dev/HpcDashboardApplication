package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.results.HpcDashboard.dto.AverageResultId;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "average_result")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Builder
@IdClass(AverageResultId.class)
@Entity
public class AverageResult implements Serializable {

    @Id
    String cpuSku;

    @Id
    int nodes;

    @Id
    String bmName;

    int cores;

    String segment;

    String appName;

    double avgResult;

    double perCorePerf;

    double perfPerDollar;

    double perfPerWatt;

    double perfPerCoreHIB;

    double averagePerfHIB;

    double coefficientOfVariation;

    int runCount;

    @Id
    String runType;

    String workload;

}
