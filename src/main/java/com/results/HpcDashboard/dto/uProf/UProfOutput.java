package com.results.HpcDashboard.dto.uProf;

import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UProfOutput {

    Set<String> benchmarks;

    List<String> metrics;

    private List<UProfDataset> dataset;
}
