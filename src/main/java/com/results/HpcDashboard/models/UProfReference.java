package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "uprofReference")
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class UProfReference implements Serializable {

    @Id
    @Column(nullable = false)
    @JsonAlias({"processor","proc", "cpu"})
    String processor;

    @JsonProperty("cpu_utilization")
    double Cpu_Utilization;

    @JsonProperty("cpu_eff_freq")
    double Cpu_Eff_Freq;

    @JsonAlias({"ipc"})
    double IPC;

    @JsonProperty("retired_sse_avx_flops")
    double Retired_SSE_AVX_Flops;

    @JsonProperty("l3_hit")
    double L3_Hit;

    @JsonProperty("total_mem_bw")
    double Total_Mem_Bw;

    @JsonProperty("total_mem_rdbw")
    double Total_Mem_RdBw;

    @JsonProperty("total_mem_wrbw")
    double Total_Mem_WrBw;

    @JsonProperty("total_xgmi0_bw")
    double Total_xGMI0_BW;
    
}