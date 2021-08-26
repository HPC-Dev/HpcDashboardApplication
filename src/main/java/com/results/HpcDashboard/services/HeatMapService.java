package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.HeatMap;
import com.results.HpcDashboard.repo.AverageResultRepo;
import com.results.HpcDashboard.repo.HeatMapRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class HeatMapService {

    @Autowired
    AverageResultRepo averageResultRepo;

    @Autowired
    HeatMapRepo heatMapRepo;

    @Transactional
    public void updateHeatResult(String segment, String isv, String cpu_sku, int nodes, String bm_name,double avg,double perCorePerf,double perfPerDollar,double perfPerWatt , int count, String runType, String category) {
        if(cpu_sku == "" || cpu_sku.equals(null) || bm_name == "" || bm_name.equals(null))
            return;
        heatMapRepo.updateHeatResult(segment,isv, bm_name,cpu_sku,nodes,avg, perCorePerf,perfPerDollar,perfPerWatt, count, runType, category);
    }

    public List<HeatMap> getHeatMapResults(String bm_name) {

        return heatMapRepo.getHeatMapResults(bm_name);
    }

    @Transactional
    public void deleteHeatResult(String cpu_sku, int nodes, String bm_name, String runType) {
        if(cpu_sku == "" || cpu_sku.equals(null) || bm_name == "" || bm_name.equals(null) )
            return;
        heatMapRepo.deleteHeatResult(bm_name,cpu_sku,nodes, runType);
    }

    @Transactional
    public void insertHeatResult(HeatMap heatResult) {
        if (heatResult== null || heatResult.getCpuSku() == "" || heatResult.getCpuSku().equals(null) || heatResult.getBmName() == "" || heatResult.getBmName().equals(null) )
            return;
        HeatMap heatMap = HeatMap.builder().segment(heatResult.getSegment()).cores(heatResult.getCores()).perCorePerf(heatResult.getPerCorePerf())
                .perfPerDollar(heatResult.getPerfPerDollar()).perfPerWatt(heatResult.getPerfPerWatt()).isv(heatResult.getIsv())
                .appName(heatResult.getAppName().trim()).avgResult(heatResult.getAvgResult()).bmName(heatResult.getBmName().trim())
                .cpuSku(heatResult.getCpuSku().trim()).nodes(heatResult.getNodes()).runCount(heatResult.getRunCount()).runType(heatResult.getRunType()).category(heatResult.getCategory()).build();
        heatMapRepo.save(heatMap);
    }


    public HeatMap getSingleHeatResult(String bm_name, String cpu_sku, int nodes, String runType) {

        HeatMap list = null;
        list = heatMapRepo.getHeatResult(bm_name,cpu_sku,nodes,runType);

        if(list ==null){
            return null;
        }
        return list;
    }


    public List<AverageResult> getAvgResultCPUApp(String cpu_sku,String app_name) {

        List<AverageResult> list = null;
        list = averageResultRepo.getAverageResultCPUApp(cpu_sku,app_name);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;
    }


    public List<String> getApp() {

        List<String> app_list = null;
        app_list = averageResultRepo.getAPP();

        if(app_list ==null){
            return Collections.EMPTY_LIST;
        }
        return app_list;
    }


    public List<HeatMap> getHeatMapData(String cpu, String type, String[] categories) {
        List<HeatMap> list = null;

        if(categories == null)
        list = heatMapRepo.findHeatMapData(cpu,type);
        else
        list = heatMapRepo.findHeatMapDataCategories(cpu,type,categories);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;

    }

    public List<HeatMap> getHeatMapData(String cpu, String type, String segment) {
        List<HeatMap> list = null;
        list = heatMapRepo.findHeatMapData(cpu,type, segment);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;

    }

    public List<HeatMap> getHeatMapDataISV(String cpu, String type, String isv, String segment) {
        List<HeatMap> list = null;
        list = heatMapRepo.findHeatMapDataISV(cpu,type, isv, segment);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;

    }

    public List<HeatMap> getHeatMapDataISV(String cpu, String type, String isv, String[] categories) {
        List<HeatMap> list = null;

        if(categories == null)
            list = heatMapRepo.findHeatMapDataISV(cpu,type, isv);
        else
            list = heatMapRepo.findHeatMapDataISVCategories(cpu,type, isv,categories);

        if(list ==null){
            return Collections.EMPTY_LIST;
        }
        return list;

    }

}
