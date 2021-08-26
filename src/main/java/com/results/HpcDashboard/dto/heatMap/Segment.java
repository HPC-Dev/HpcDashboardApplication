package com.results.HpcDashboard.dto.heatMap;

import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class Segment {
    String Segment;
    double uplift;
    double per_Core_Uplift;
    double per_Dollar_Uplift;
    double per_Watt_Uplift;
    Set<ISV> isvList;
}
