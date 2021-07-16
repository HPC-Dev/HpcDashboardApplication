package com.results.HpcDashboard.dto.uProf;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UProfId implements Serializable {


    String procAppBm;

    String runType;
}
