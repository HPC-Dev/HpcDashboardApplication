package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.Result;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ResultRepo extends DataTablesRepository<Result, String> {

    public static final String FIND_RESULTS_APP_CPU_NODE = "SELECT result FROM results where app_name=:app_name and bm_name=:bm_name and cpu=:cpu and nodes=:nodes and run_type=:runType ";
    public static final String GET_CPU = "select DISTINCT cpu from results ORDER BY cpu ASC";
    public static final String GET_APP = "select DISTINCT LOWER(app_name) from results ORDER BY app_name ASC;";
    public static final String GET_BM = "select DISTINCT bm_name from results ORDER BY bm_name ASC";
    public static final String GET_NODES = "select DISTINCT nodes from results ORDER BY nodes ASC";

    public static final String GET_OS = "select DISTINCT LOWER(os) from results ORDER BY os ASC";

    public static final String GET_BIOS = "select DISTINCT bios_ver from results ORDER BY os ASC";

    public static final String GET_CLUSTER = "select DISTINCT LOWER(cluster) from results ORDER BY cluster ASC";
    public static final String GET_USERS = "select DISTINCT LOWER(user) from results ORDER BY user ASC";
    public static final String GET_PLATFORM = "select DISTINCT LOWER(platform) from results ORDER BY platform ASC";
    public static final String GET_CPU_GEN = "select DISTINCT LOWER(cpu_gen) from results ORDER BY cpu_gen ASC";
    public static final String GET_RUN_TYPE = "select DISTINCT LOWER(run_type) from results ORDER BY run_type ASC";
    public static final String GET_CPU_BASED_GEN = "select DISTINCT cpu from results where cpu_gen=:cpuGen ORDER BY cpu_gen ASC;";

    @Query(value = FIND_RESULTS_APP_CPU_NODE, nativeQuery = true)
    public List<Double> findresultsByAppCPUNode(String app_name,String bm_name, String cpu, int nodes, String runType);

    @Query(value = GET_CPU, nativeQuery = true)
    List<String> getCpu();

    @Query(value = GET_APP, nativeQuery = true)
    List<String> getApp();

    @Query(value = GET_BM, nativeQuery = true)
    List<String> getBm();

    @Query(value = GET_NODES, nativeQuery = true)
    List<Integer> getNodes();

    @Query(value = GET_OS, nativeQuery = true)
    List<String> getOS();

    @Query(value = GET_BIOS, nativeQuery = true)
    List<String> getBIOS();

    @Query(value = GET_CLUSTER, nativeQuery = true)
    List<String> getCluster();

    @Query(value = GET_USERS, nativeQuery = true)
    List<String> getUsers();

    @Query(value = GET_PLATFORM, nativeQuery = true)
    List<String> getPlatform();

    @Query(value = GET_CPU_GEN, nativeQuery = true)
    List<String> getCpuGen();

    @Query(value = GET_RUN_TYPE, nativeQuery = true)
    List<String> getRunType();

    @Query(value = GET_CPU_BASED_GEN, nativeQuery = true)
    List<String> getCPUGen(String cpuGen);

}
