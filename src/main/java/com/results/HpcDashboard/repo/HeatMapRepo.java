package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.dto.HeatMapId;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.HeatMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeatMapRepo extends JpaRepository<HeatMap, HeatMapId> {

    public static final String UPDATE_HEAT_RESULT = "UPDATE heat_map set segment=:segment, isv=:isv, avg_result=:avg,per_core_perf=:perCorePerf,perf_per_dollar=:perfPerDollar,perf_per_watt=:perfPerWatt, run_count=:count, category=:category where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes and run_type=:runType";
    public static final String DELETE_HEAT_RESULT = "DELETE FROM heat_map where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes and run_type=:runType";
    public static final String GET_HEAT_RESULT = "SELECT * from heat_map where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes and run_type=:runType";
    public static final String GET_HEAT_MAP_DATA ="select * from heat_map where cpu_sku=:cpu and run_type=:type and nodes=1 and segment is not NULL";
    public static final String GET_HEAT_MAP_DATA_SEGMENT="select * from heat_map where cpu_sku=:cpu and run_type=:type and nodes=1 and segment=:segment";

    public static final String GET_HEAT_MAP_DATA_CATEGORIES="select * from heat_map where cpu_sku=:cpu and run_type=:type and nodes=1 and segment is not NULL and category IN (:categories)";


    public static final String GET_HEAT_MAP_DATA_ISV="select * from heat_map where cpu_sku=:cpu and run_type=:type and nodes=1 and isv=:isv";
    public static final String GET_HEAT_MAP_DATA_ISV_SEGMENT="select * from heat_map where cpu_sku=:cpu and run_type=:type and nodes=1 and isv=:isv and segment=:segment";

    public static final String GET_HEAT_MAP_DATA_ISV_CATEGORIES="select * from heat_map where cpu_sku=:cpu and run_type=:type and nodes=1 and isv=:isv and category IN (:categories)";

    public static final String GET_HEAT_MAP_RESULTS= "SELECT * from heat_map where bm_name=:bm_name";

    @Modifying
    @Query(value = UPDATE_HEAT_RESULT, nativeQuery = true)
    void updateHeatResult(String segment, String isv, String bm_name,String cpu_sku, int nodes, double avg,double perCorePerf,double perfPerDollar,double perfPerWatt ,int count,String runType, String category);

    @Modifying
    @Query(value = DELETE_HEAT_RESULT, nativeQuery = true)
    void deleteHeatResult(String bm_name, String cpu_sku, int nodes, String runType);

    @Query(value = GET_HEAT_RESULT, nativeQuery = true)
    HeatMap getHeatResult(String bm_name, String cpu_sku, int nodes, String runType);

    @Query(nativeQuery =true,value = GET_HEAT_MAP_DATA)
    List<HeatMap> findHeatMapData(String cpu, String type);

    @Query(nativeQuery =true,value = GET_HEAT_MAP_DATA_CATEGORIES)
    List<HeatMap> findHeatMapDataCategories(String cpu, String type, String[] categories);

    @Query(nativeQuery =true,value = GET_HEAT_MAP_DATA_SEGMENT)
    List<HeatMap> findHeatMapData(String cpu, String type, String segment);


    @Query(nativeQuery =true,value = GET_HEAT_MAP_DATA_ISV)
    List<HeatMap> findHeatMapDataISV(String cpu, String type, String isv);

    @Query(nativeQuery =true,value = GET_HEAT_MAP_DATA_ISV_CATEGORIES)
    List<HeatMap> findHeatMapDataISVCategories(String cpu, String type, String isv, String[] categories);

    @Query(nativeQuery =true,value = GET_HEAT_MAP_DATA_ISV_SEGMENT)
    List<HeatMap> findHeatMapDataISV(String cpu, String type, String isv, String segment);

    @Query(nativeQuery =true,value = GET_HEAT_MAP_RESULTS)
    List<HeatMap> getHeatMapResults(String bm_name);
}
