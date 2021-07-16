package com.results.HpcDashboard.dto.uProf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UProfDto implements Serializable {

    double Cpu_Utilization;

    double Cpu_Eff_Freq;

    double IPC;

    double Retired_SSE_AVX_Flops;

    double L3_Miss;

    double Total_Mem_Bw;

    double Total_Mem_RdBw;

    double Total_Mem_WrBw;

    double Total_xGMI0_BW;

    public UProfDto(Object[] columns) {

        this.Cpu_Utilization = (double) columns[0];
        this.Cpu_Eff_Freq = (double) columns[1];
        this.IPC = (double) columns[2];
        this.Retired_SSE_AVX_Flops = (double) columns[3];
        this.L3_Miss = (double) columns[4];
        this.Total_Mem_Bw = (double) columns[5];
        this.Total_Mem_RdBw = (double) columns[6];
        this.Total_Mem_WrBw = (double) columns[7];
        this.Total_xGMI0_BW = (double) columns[8];


    }
}
