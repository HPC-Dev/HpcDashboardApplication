package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.dto.AverageResultId;
import com.results.HpcDashboard.models.AverageResult;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AverageResultRepo extends DataTablesRepository<AverageResult, AverageResultId> {

    public static final String UPDATE_AVG_RESULT = "UPDATE average_result set segment=:segment, avg_result=:avg,per_core_perf=:perCorePerf,perf_per_dollar=:perfPerDollar,perf_per_watt=:perfPerWatt, coefficient_of_variation=:cv, run_count=:count, workload=:workload,average_perfhib=:averagePerfHIB,perf_per_corehib=:perfPerCoreHIB where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes and run_type=:runType";
    public static final String DELETE_AVG_RESULT = "DELETE FROM average_result where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes and run_type=:runType";
    public static final String GET_AVG_RESULT = "SELECT * from average_result where bm_name=:bm_name and cpu_sku =:cpu_sku and nodes=:nodes and run_type=:runType";
    public static final String GET_AVG_RESULT_CPU_APP = "SELECT * from average_result where cpu_sku =:cpu_sku and app_name =:app_name ORDER BY bm_name";
    public static final String GET_AVG_RESULT_CPU_APP_TYPE = "SELECT * from average_result where cpu_sku =:cpu_sku and app_name =:app_name and run_type=:type ORDER BY bm_name";
    public static final String GET_AVG_RESULT_CPU_APP_BM = "SELECT * from average_result where cpu_sku =:cpu_sku and app_name =:app_name and bm_name =:bm_name";
    public static final String GET_AVG_RESULT_CPU_APP_NODE = "SELECT * from average_result where cpu_sku =:cpu_sku and app_name =:app_name and nodes =:nodes and run_type=:type";
    public static final String GET_CPU = "select DISTINCT cpu_sku from average_result ORDER BY cpu_sku ASC";
    public static final String GET_CPU_APP = "select DISTINCT cpu_sku from average_result where app_name=:appName and nodes=1 ORDER BY cpu_sku ASC";
    public static final String GET_CPU_WORKLOAD = "select DISTINCT cpu_sku from average_result where workload IN (:workloads) and nodes=1 ORDER BY cpu_sku ASC";
    public static final String GET_RUN_TYPES = "SELECT  DISTINCT LOWER(run_type) FROM  average_result WHERE app_name=:appName ORDER BY run_type ASC";
    public static final String GET_RUN_TYPES_WORKLOADS = "SELECT  DISTINCT LOWER(run_type) FROM  average_result WHERE app_name=:appName and workload IN (:workloads) ORDER BY run_type ASC";
    public static final String GET_RUN_TYPES_BY_CPU = "SELECT  DISTINCT LOWER(run_type) FROM  average_result WHERE cpu_sku=:cpu ORDER BY run_type ASC";
    public static final String GET_RUN_TYPES_BY_APP_CPU = "SELECT  DISTINCT LOWER(run_type) FROM  average_result WHERE cpu_sku=:cpu and app_name=:appName ORDER BY run_type ASC";
    public static final String GET_CPU_SELECTED = "SELECT DISTINCT a.cpu_sku FROM  average_result AS a WHERE  a.app_name = :appName and a.nodes=1 and a.run_type IN (:runTypes) ORDER BY cpu_sku ASC";

    public static final String GET_CPU_SELECTED_WORKLOADS = "SELECT DISTINCT a.cpu_sku FROM  average_result AS a WHERE  a.app_name = :appName and a.nodes=1 and a.run_type IN (:runTypes) and a.workload IN (:workloads) ORDER BY cpu_sku ASC";

    public static final String NODES_COUNT = "select count(distinct nodes) from average_result where  app_name=:appName and cpu_sku=:cpu and run_type=:type";
    public static final String GET_CPU_RES = "select DISTINCT cpu_sku from average_result ORDER BY cpu_sku ASC";
    public static final String GET_APP = "select DISTINCT LOWER(app_name) from average_result ORDER BY app_name ASC";
    public static final String GET_APP_CPU = "select DISTINCT LOWER(app_name) from average_result where cpu_sku=:cpu ORDER BY app_name ASC;";
    public static final String GET_APP_WORKLOAD = "select DISTINCT LOWER(app_name) from average_result where workload IN (:workloads) ORDER BY app_name ASC;";

    public static final String GET_APP_TYPE = "select DISTINCT LOWER(app_name) from average_result where cpu_sku=:cpu and run_type=:runType ORDER BY app_name ASC;";
    public static final String GET_SELECTED_CPU_RES_BY_AVG = "select * from average_result where app_name= :app_name and cpu_sku IN (:cpus) and run_type IN (:runType) and nodes =1 ORDER BY bm_name";
    public static final String GET_COMP_CPU_RES = "select * from average_result where app_name= :app_name and cpu_sku =:cpu and nodes =1 and run_type=:runType ORDER BY avg_result";
    public static final String GET_SCALING_CPU_RES = "select * from average_result where app_name= :app_name and cpu_sku =:cpu and run_type=:runType ORDER BY avg_result";
    public static final String GET_SELECTED_BM_CPU = "select DISTINCT bm_name from average_result where app_name=:app_name and cpu_sku=:cpu ORDER BY bm_name ASC";
    public static final String GET_SELECTED_BM = "select DISTINCT bm_name from average_result where app_name=:app_name ORDER BY bm_name ASC";
    public static final String Job_EXISTS ="select count(*) from results where job_id=:jobId";
    public static final String GET_SELECTED_CPU_RES_BY_AVG_New_ASC = "select * from average_result where app_name= :app_name and cpu_sku IN (:cpus) and run_type IN (:runTypes) and nodes =1 ORDER BY avg_result";
    public static final String GET_SELECTED_CPU_RES_BY_AVG_New_DESC = "select * from average_result where app_name= :app_name and cpu_sku IN (:cpus)and run_type IN (:runTypes) and nodes =1 ORDER BY avg_result DESC";
    public static final String GET_RUN_COUNT = "select DISTINCT run_count from average_result ORDER BY run_count ASC";
    public static final String GET_SEGMENTS = "select DISTINCT(segment) from average_result ORDER BY workload ASC";

    @Modifying
    @Query(value = UPDATE_AVG_RESULT, nativeQuery = true)
    void updateAverageResult(String segment,String bm_name, String cpu_sku, int nodes, double avg,double perCorePerf,double perfPerDollar,double perfPerWatt, double cv, int count, String runType, String workload, double averagePerfHIB, double perfPerCoreHIB);

    @Modifying
    @Query(value = DELETE_AVG_RESULT, nativeQuery = true)
    void deleteAverageResult(String bm_name, String cpu_sku, int nodes, String runType);

    @Query(value = GET_AVG_RESULT, nativeQuery = true)
    AverageResult getAverageResult(String bm_name, String cpu_sku, int nodes, String runType);

    @Query(value = GET_AVG_RESULT_CPU_APP, nativeQuery = true)
    List<AverageResult> getAverageResultCPUApp(String cpu_sku, String app_name);

    @Query(value = GET_AVG_RESULT_CPU_APP_TYPE, nativeQuery = true)
    List<AverageResult> getAverageResultCPUAppType(String cpu_sku, String app_name, String type);

    @Query(value = GET_AVG_RESULT_CPU_APP_BM, nativeQuery = true)
    List<AverageResult> getAverageResultCPUAppBm(String cpu_sku, String app_name, String bm_name);

    @Query(value = GET_AVG_RESULT_CPU_APP_NODE, nativeQuery = true)
    List<AverageResult> getAverageResultCPUAppNode(String cpu_sku, String app_name, int nodes, String type);

    @Query(value = GET_APP, nativeQuery = true)
    List<String> getAPP();

    @Query(value = GET_APP_CPU, nativeQuery = true)
    List<String> getAPP(String cpu);

    @Query(value = GET_APP_WORKLOAD, nativeQuery = true)
    List<String> getAPP(String[] workloads);

    @Query(value = GET_APP_TYPE, nativeQuery = true)
    List<String> getAppByType(String cpu, String runType);

    @Query(value = GET_CPU, nativeQuery = true)
    List<String> getJustCPU();

    @Query(value = GET_CPU_APP, nativeQuery = true)
    List<String> getCPU(String appName);

    @Query(value = GET_RUN_TYPES, nativeQuery = true)
    List<String> getRunTypes(String appName);

    @Query(value = GET_RUN_TYPES_WORKLOADS, nativeQuery = true)
    List<String> getRunTypes(String appName, String[] workloads);

    @Query(value = GET_RUN_TYPES_BY_CPU, nativeQuery = true)
    List<String> getRunTypesByCPU(String cpu);

    @Query(value = GET_RUN_TYPES_BY_APP_CPU, nativeQuery = true)
    List<String> getRunTypesByAPPCPU(String appName, String cpu);

    @Query(nativeQuery =true,value = GET_CPU_SELECTED)
    List<String> getCpuSelected(String appName,  List<String> runTypes);

    @Query(nativeQuery =true,value = GET_CPU_SELECTED_WORKLOADS)
    List<String> getCpuSelected(String appName,  List<String> runTypes, String[] workloads);

    @Query(value = GET_CPU_RES, nativeQuery = true)
    List<String> getCPU();


    @Query(nativeQuery =true,value = GET_SELECTED_CPU_RES_BY_AVG_New_ASC)
    List<AverageResult> findBySelectedCPUAppAsc(String app_name, List<String> cpus, List<String> runTypes);


    @Query(nativeQuery =true,value = GET_SELECTED_CPU_RES_BY_AVG_New_DESC)
    List<AverageResult> findBySelectedCPUAppDesc(String app_name, List<String> cpus, List<String> runTypes);

    @Query(nativeQuery =true,value = GET_COMP_CPU_RES)
    List<AverageResult> findCompDataBySelectedCPU(String app_name, String cpu, String runType);

    @Query(nativeQuery =true,value = GET_SCALING_CPU_RES)
    List<AverageResult> findScalingDataBySelectedCPU(String app_name, String cpu, String runType);


    @Query(value = GET_SELECTED_BM_CPU, nativeQuery = true)
    List<String> getSelectedBm(String app_name, String cpu);

    @Query(value = GET_SELECTED_BM, nativeQuery = true)
    List<String> getSelectedBm(String app_name);

    @Query(value = Job_EXISTS, nativeQuery = true)
    int getJobExists(String jobId);

    @Query(value = NODES_COUNT, nativeQuery = true)
    int getNodesCount(String appName, String cpu , String type);

    @Query(value = GET_RUN_COUNT, nativeQuery = true)
    List<Integer> getRunCount();

    @Query(nativeQuery =true,value = GET_SELECTED_CPU_RES_BY_AVG)
    List<AverageResult> findBySelectedCPUApp(String app_name, List<String> cpus, List<String> runType );

    @Query(value = GET_CPU_WORKLOAD, nativeQuery = true)
    List<String> getCpuWorkloads(String[] workloads);

    @Query(value = GET_SEGMENTS, nativeQuery = true)
    List<String> getSegments();
}
