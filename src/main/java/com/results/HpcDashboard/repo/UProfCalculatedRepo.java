package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.UProfCalculated;
import com.results.HpcDashboard.models.UProfRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UProfCalculatedRepo extends JpaRepository<UProfCalculated,Integer> {

    public static final String FIND_UProf_Calculated = "select * from uprof_calculated WHERE proc_app_bm=:cpu and run_type=:run_type";
    public static final String GET_CPU = "select DISTINCT proc_app_bm from uprof_calculated ORDER BY proc_app_bm ASC";
    public static final String GET_RUN_TYPES_BY_CPUUProf = "SELECT  DISTINCT LOWER(run_type) FROM  uprof_calculated WHERE proc_app_bm=:cpu ORDER BY run_type ASC";

    @Query(value = FIND_UProf_Calculated, nativeQuery = true)
    public UProfCalculated findUProf_Calculated(String cpu, String run_type);

    @Query(value = GET_CPU, nativeQuery = true)
    public List<String> findAllCPUs();

    @Query(value = GET_RUN_TYPES_BY_CPUUProf, nativeQuery = true)
    List<String> getRunTypesByCPUUProf(String cpu);
}