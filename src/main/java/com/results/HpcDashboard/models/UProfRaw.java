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

    double Core_8_Utilization;

    double Core_8_Eff_Freq;

    double Core_8_IPC;

    double Core_8_CPI;

    double Core_8_Branch_Misprediction_Ratio;

    double Core_8_Retired_SSE_AVX_Flops;

    double Core_8_Mixed_SSE_AVX_Stalls;

    double CCX_0_L3_Access;

    double CCX_0_L3_Miss;

    double CCX_0_L3_Miss_percent;

    double CCX_0_Ave_L3_Miss_Latency;

    double CCX_1_L3_Access;

    double CCX_1_L3_Miss;

    double CCX_1_L3_Miss_percent;

    double CCX_1_Ave_L3_Miss_Latency;

    double CCX_2_L3_Access;

    double CCX_2_L3_Miss;

    double CCX_2_L3_Miss_percent;

    double CCX_2_Ave_L3_Miss_Latency;

    double CCX_3_L3_Access;

    double CCX_3_L3_Miss;

    double CCX_3_L3_Miss_percent;

    double CCX_3_Ave_L3_Miss_Latency;

    double CCX_4_L3_Access;

    double CCX_4_L3_Miss;

    double CCX_4_L3_Miss_percent;

    double CCX_4_Ave_L3_Miss_Latency;

    double CCX_5_L3_Access;

    double CCX_5_L3_Miss;

    double CCX_5_L3_Miss_percent;

    double CCX_5_Ave_L3_Miss_Latency;

    double CCX_6_L3_Access;

    double CCX_6_L3_Miss;

    double CCX_6_L3_Miss_percent;

    double CCX_6_Ave_L3_Miss_Latency;

    double CCX_7_L3_Access;

    double CCX_7_L3_Miss;

    double CCX_7_L3_Miss_percent;

    double CCX_7_Ave_L3_Miss_Latency;

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