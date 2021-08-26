package com.results.HpcDashboard.dto.heatMap;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class HeatMapResult {

    String Segment;
    String ISV;
    String Application;
    String Benchmark;

    String perNode1;
    String perCore1;
    String perDollar1;
    String perWatt1;

    String perNode2;
    String perCore2;
    String perDollar2;
    String perWatt2;

    String perNode3;
    String perCore3;
    String perDollar3;
    String perWatt3;
}
