package com.results.HpcDashboard.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "uprofRaw")
@Builder
public class UProfRaw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int counter;

    @Column(nullable = false)
    String procAppBm;

    @Column(nullable = false)
    String runType;

    double Core_0_Utilization;

    double Core_0_Eff_Freq;

    double Core_0_IPC;

    double Core_0_CPI;

    double Core_0_Branch_Misprediction_Ratio;

    double Core_0_Retired_SSE_AVX_Flops;

    double Core_0_Mixed_SSE_AVX_Stalls;

    double CCX_0_L3_Access;

    double CCX_0_L3_Miss;

    double CCX_0_L3_Miss_percent;

    double CCX_0_Ave_L3_Miss_Latency;

    double CCX_1_L3_Access;

    double CCX_1_L3_Miss;

    double CCX_1_L3_Miss_percent;

    double CCX_1_Ave_L3_Miss_Latency;

    double Package_0_Total_Mem_Bw;

    double Package_0_Total_Mem_RdBw;

    double Package_0_Total_Mem_WrBw;

    double Package_0_Mem_Ch_A_RdBw;

    double Package_0_Mem_Ch_A_WrBw;

    double Package_0_Mem_Ch_B_RdBw;

    double Package_0_Mem_Ch_B_WrBw;

    double Package_0_Mem_Ch_C_RdBw;

    double Package_0_Mem_Ch_C_WrBw;

    double Package_0_Mem_Ch_D_RdBw;

    double Package_0_Mem_Ch_D_WrBw;

    double Package_0_Mem_Ch_E_RdBw;

    double Package_0_Mem_Ch_E_WrBw;

    double Package_0_Mem_Ch_F_RdBw;

    double Package_0_Mem_Ch_F_WrBw;

    double Package_0_Mem_Ch_G_RdBw;

    double Package_0_Mem_Ch_G_WrBw;

    double Package_0_Mem_Ch_H_RdBw;

    double Package_0_Mem_Ch_H_WrBw;

    double Package_0_Approximate_xGMI_outbound_data_bytes;

    double Package_0_xGMI0_BW;

    double Package_0_xGMI1_BW;

    double Package_0_xGMI2_BW;

    double Package_0_xGMI3_BW;

}