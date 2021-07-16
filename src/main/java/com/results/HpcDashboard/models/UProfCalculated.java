package com.results.HpcDashboard.models;

import com.results.HpcDashboard.dto.uProf.UProfId;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "uprofCalculated")
@IdClass(UProfId.class)
@Builder
public class UProfCalculated {

    @Id
    @Column(nullable = false)
    String procAppBm;

    @Id
    @Column(nullable = false)
    String runType;

    String processor;

    double Cpu_Utilization;

    double Cpu_Eff_Freq;

    double IPC;

    double Retired_SSE_AVX_Flops;

    double L3_Hit;

    double Total_Mem_Bw;

    double Total_Mem_RdBw;

    double Total_Mem_WrBw;

    double Total_xGMI0_BW;

}