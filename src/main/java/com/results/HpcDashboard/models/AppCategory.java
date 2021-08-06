package com.results.HpcDashboard.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.results.HpcDashboard.dto.AverageResultId;
import com.results.HpcDashboard.dto.HeatMapId;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "app_category")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Builder
public class AppCategory {

    @JsonAlias({"Category"})
    String category;

    @JsonAlias({"ISV"})
    String isv;

    @JsonAlias({"App Name, App_name, Application"})
    String appName;

    @Id
    @JsonAlias({"Benchmark"})
    String bmName;

    @JsonAlias({"Workload"})
    String workload;
}

