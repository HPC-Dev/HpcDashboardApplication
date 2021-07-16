package com.results.HpcDashboard.dto.uProf;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UProfDataset {
    private String procAppBM;
    private List<Integer> value;
}
