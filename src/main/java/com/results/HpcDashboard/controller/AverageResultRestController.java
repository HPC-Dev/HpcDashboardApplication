package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.heatMap.*;
import com.results.HpcDashboard.dto.partComparison.*;
import com.results.HpcDashboard.models.AppMap;
import com.results.HpcDashboard.models.AverageResult;
import com.results.HpcDashboard.models.HeatMap;
import com.results.HpcDashboard.repo.AppMapRepo;
import com.results.HpcDashboard.services.AverageResultService;
import com.results.HpcDashboard.services.HeatMapService;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/avg")
public class AverageResultRestController {

    @Autowired
    AverageResultService averageResultService;

    @Autowired
    Util util;

    @Autowired
    HeatMapService heatMapService;

    @Autowired
    ChartRestController chartRestController;

    @Autowired
    AppMapRepo appMapRepo;

    @Autowired
    AverageResultRestController averageResultRestController;


    public String getLowerHigher(String app) {
        List<AppMap> appMaps = appMapRepo.findAllAppMap();
        String appStatus = "";

        for (int i = 0; i < appMaps.size(); i++) {
            if (appMaps.get(i).getAppName().toLowerCase().equals(app.toLowerCase())) {
                appStatus = appMaps.get(i).getStatus();
            }

        }
        return appStatus;
    }


    @GetMapping("/result")
    public List<AverageResult> getAvgResult() {
        List<AverageResult> list = null;
        list = averageResultService.getAverageResult();
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }


    @GetMapping("/result/{cpu}/{app_name}/{runType}")
    public List<AverageResult> getAvgResultCPU(@PathVariable("cpu") String cpu, @PathVariable("app_name") String app_name, @PathVariable("runType") String runType) {
        List<AverageResult> list = null;
        list = averageResultService.getAvgResultCPUAppType(cpu, app_name, runType);
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }

    public static Map<Integer, Map<String, Double>> insertScalingIntoHashMap(List<AverageResult> list1) {
        Map<Integer, Map<String, Double>> result1 = new HashMap<>();
        Map<String, Double> r1;
        for (AverageResult a : list1) {
            if (result1.containsKey(a.getNodes())) {
                r1 = result1.get(a.getNodes());
                r1.put(a.getBmName(), a.getAvgResult());
            } else {
                r1 = new HashMap<>();
                r1.put(a.getBmName(), a.getAvgResult());
                result1.put(a.getNodes(), r1);
            }
        }
        return result1;
    }


    public static Map<Integer, Map<String, Double>> insertScalingIntoHashMapPerCore(List<AverageResult> list1) {
        Map<Integer, Map<String, Double>> result1 = new HashMap<>();
        Map<String, Double> r1;
        for (AverageResult a : list1) {
            if (result1.containsKey(a.getNodes())) {
                r1 = result1.get(a.getNodes());
                r1.put(a.getBmName(), a.getPerCorePerf());
            } else {
                r1 = new HashMap<>();
                r1.put(a.getBmName(), a.getPerCorePerf());
                result1.put(a.getNodes(), r1);
            }
        }
        return result1;
    }

    public static Map<Integer, Map<String, Double>> insertScalingIntoHashMapPerCount(List<AverageResult> list1) {
        Map<Integer, Map<String, Double>> result1 = new HashMap<>();
        Map<String, Double> r1;
        for (AverageResult a : list1) {
            if (result1.containsKey(a.getNodes())) {
                r1 = result1.get(a.getNodes());
                r1.put(a.getBmName(), Double.valueOf(a.getRunCount()));
            } else {
                r1 = new HashMap<>();
                r1.put(a.getBmName(), Double.valueOf(a.getRunCount()));
                result1.put(a.getNodes(), r1);
            }
        }
        return result1;
    }

    public static Map<Integer, Map<String, Double>> insertScalingIntoHashMapPerVariance(List<AverageResult> list1) {
        Map<Integer, Map<String, Double>> result1 = new HashMap<>();
        Map<String, Double> r1;
        for (AverageResult a : list1) {
            if (result1.containsKey(a.getNodes())) {
                r1 = result1.get(a.getNodes());
                r1.put(a.getBmName(), a.getCoefficientOfVariation());
            } else {
                r1 = new HashMap<>();
                r1.put(a.getBmName(), a.getCoefficientOfVariation());
                result1.put(a.getNodes(), r1);
            }
        }
        return result1;
    }

    public List<Map<String, String>>  scalingCalculation(Set<String> bms, String app_name, Set<Integer> keyset1, Map<Integer, Map<String, Double>> result1, Map<Integer, Map<String, Double>> result2 ){

        List<Map<String, String>> resultData = new ArrayList<>();

        for (int key : keyset1) {
            Map<String, Double> r1 = result1.get(key);
            Map<String, Double> r2 = result2.get(key);
            LinkedHashMap<String, String> hashmap;

            hashmap = new LinkedHashMap<>();

            hashmap.put("Nodes", String.valueOf(key));

            for (String bm : bms) {
                double val1 = r1.getOrDefault(bm, 0.0);
                double val2 = r2.getOrDefault(bm, 0.0);

                if (val1 != 0.0 && val2 != 0.0) {
                    double relativeValue = 0.0;
                    if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                        if (Double.compare(val1, val2) == 0) {
                            relativeValue = 1;
                        } else {
                            relativeValue = util.round( (val2 / val1), 3);
                        }
                    } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")) {
                        if (Double.compare(val1, val2) == 0) {
                            relativeValue = 1;
                        } else {
                            Double d = val1/val2;
                            relativeValue = util.round( (val1 / val2), 3);
                        }
                    }
                    hashmap.put(bm, String.valueOf(relativeValue));
                }

            }

            resultData.add(hashmap);

        }
        return resultData;

    }

    public static Set<String> bmsList(List<AverageResult> list1, List<AverageResult> list2){
        Set<String> bms = new LinkedHashSet<>();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).getBmName().equals(list2.get(j).getBmName())) {
                    bms.add(list1.get(i).getBmName());
                }
            }
        }
        return bms;

    }

    @GetMapping("/scalingComparisonNode/{app_name}/{cpu1}/{cpu2}/{type1}/{type2}")
    public CompareResult getScalingComparison(@PathVariable("app_name") String app_name, @PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2, @PathVariable("type1") String type1, @PathVariable("type2") String type2) {
        CompareResult compareResult = null;
        List<AverageResult> list1 = averageResultService.getScalingDataBySelectedCPU(app_name, cpu1, type1);
        List<AverageResult> list2 = averageResultService.getScalingDataBySelectedCPU(app_name, cpu2, type2);
        Set<String> bms = bmsList(list1,list2);

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 ||  bms.size() < 1 )
            return compareResult;


        Map<Integer, Map<String, Double>> result1 = insertScalingIntoHashMap(list1);

        Map<Integer, Map<String, Double>> result2 = insertScalingIntoHashMap(list2);


        Set<Integer> keyset1 = result1.keySet();
        Set<Integer> keyset2 = result2.keySet();

        keyset1.retainAll(keyset2);
        keyset2.retainAll(keyset1);

        if (keyset1.isEmpty() || keyset2.isEmpty())
            return compareResult;

        List<Map<String, String>> resultData = averageResultRestController.scalingCalculation(bms,app_name,keyset1,result1,result2);

        Set<String> bmNames = resultData.get(0).keySet();

        compareResult = CompareResult.builder().appName(app_name).bmName(bmNames).resultData(resultData).build();

        return compareResult;
    }


    @GetMapping("/scalingComparisonCore/{app_name}/{cpu1}/{cpu2}/{type1}/{type2}")
    public CompareResult getScalingComparisonCore(@PathVariable("app_name") String app_name, @PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2, @PathVariable("type1") String type1, @PathVariable("type2") String type2) {
        CompareResult compareResult = null;
        List<AverageResult> list1 = averageResultService.getScalingDataBySelectedCPU(app_name, cpu1, type1);
        List<AverageResult> list2 = averageResultService.getScalingDataBySelectedCPU(app_name, cpu2, type2);
        Set<String> bms = bmsList(list1,list2);

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 ||  bms.size() < 1 )
            return compareResult;


        Map<Integer, Map<String, Double>> result1 = insertScalingIntoHashMapPerCore(list1);

        Map<Integer, Map<String, Double>> result2 = insertScalingIntoHashMapPerCore(list2);


        Set<Integer> keyset1 = result1.keySet();
        Set<Integer> keyset2 = result2.keySet();

        keyset1.retainAll(keyset2);
        keyset2.retainAll(keyset1);

        if (keyset1.isEmpty() || keyset2.isEmpty())
            return compareResult;

        List<Map<String, String>> resultData = averageResultRestController.scalingCalculation(bms,app_name,keyset1,result1,result2);

        Set<String> bmNames = resultData.get(0).keySet();

        compareResult = CompareResult.builder().appName(app_name).bmName(bmNames).resultData(resultData).build();

        return compareResult;
    }

    @GetMapping("/scalingComparisonCount/{app_name}/{cpu1}/{cpu2}/{type1}/{type2}")
    public CompareResult getScalingComparisonCount(@PathVariable("app_name") String app_name, @PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2, @PathVariable("type1") String type1, @PathVariable("type2") String type2) {
        CompareResult compareResult = null;
        List<AverageResult> list1 = averageResultService.getScalingDataBySelectedCPU(app_name, cpu1, type1);
        List<AverageResult> list2 = averageResultService.getScalingDataBySelectedCPU(app_name, cpu2, type2);
        Set<String> bms = bmsList(list1,list2);

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 ||  bms.size() < 1 )
            return compareResult;


        Map<Integer, Map<String, Double>> result1 = insertScalingIntoHashMapPerCount(list1);

        Map<Integer, Map<String, Double>> result2 = insertScalingIntoHashMapPerCount(list2);


        Set<Integer> keyset1 = result1.keySet();
        Set<Integer> keyset2 = result2.keySet();

        keyset1.retainAll(keyset2);
        keyset2.retainAll(keyset1);

        if (keyset1.isEmpty() || keyset2.isEmpty())
            return compareResult;

        List<Map<String, String>> resultData = averageResultRestController.scalingCalculation(bms,app_name,keyset1,result1,result2);

        Set<String> bmNames = resultData.get(0).keySet();

        compareResult = CompareResult.builder().appName(app_name).bmName(bmNames).resultData(resultData).build();

        return compareResult;
    }


    @GetMapping("/scalingComparisonVariance/{app_name}/{cpu1}/{cpu2}/{type1}/{type2}")
    public CompareResult getScalingComparisonVariance(@PathVariable("app_name") String app_name, @PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2, @PathVariable("type1") String type1, @PathVariable("type2") String type2) {
        CompareResult compareResult = null;
        List<AverageResult> list1 = averageResultService.getScalingDataBySelectedCPU(app_name, cpu1, type1);
        List<AverageResult> list2 = averageResultService.getScalingDataBySelectedCPU(app_name, cpu2, type2);
        Set<String> bms = bmsList(list1,list2);

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0 ||  bms.size() < 1 )
            return compareResult;


        Map<Integer, Map<String, Double>> result1 = insertScalingIntoHashMapPerVariance(list1);

        Map<Integer, Map<String, Double>> result2 = insertScalingIntoHashMapPerVariance(list2);


        Set<Integer> keyset1 = result1.keySet();
        Set<Integer> keyset2 = result2.keySet();

        keyset1.retainAll(keyset2);
        keyset2.retainAll(keyset1);

        if (keyset1.isEmpty() || keyset2.isEmpty())
            return compareResult;

        List<Map<String, String>> resultData = averageResultRestController.scalingCalculation(bms,app_name,keyset1,result1,result2);

        Set<String> bmNames = resultData.get(0).keySet();

        compareResult = CompareResult.builder().appName(app_name).bmName(bmNames).resultData(resultData).build();

        return compareResult;
    }





    @GetMapping("/resultComparison/{app_name}/{cpu1}/{cpu2}/{type1}/{type2}")
    public CompareResult getAvgBySelectedCPU(@PathVariable("app_name") String app_name, @PathVariable("cpu1") String cpu1, @PathVariable("cpu2") String cpu2, @PathVariable("type1") String type1, @PathVariable("type2") String type2) {

        CompareResult compareResult = null;
        String comment = null;
        List<AverageResult> list1 = averageResultService.getCompDataBySelectedCPU(app_name, cpu1, type1);
        List<AverageResult> list2 = averageResultService.getCompDataBySelectedCPU(app_name, cpu2, type2);

        Set<String> bms = new LinkedHashSet<>();
        bms.add("");
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).getBmName().equals(list2.get(j).getBmName())) {
                    bms.add(list1.get(i).getBmName());
                }
            }
        }

        if (bms.size() < 2)
            return compareResult;


        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0)
            return compareResult;


        Map<String, Double> result1 = new LinkedHashMap<>();
        Map<String, Double> result2 = new LinkedHashMap<>();

        Map<String, String> perfDifference = new LinkedHashMap<>();

        for (AverageResult a : list1) {
            result1.put(a.getBmName(), a.getAvgResult());
        }
        for (AverageResult a : list2) {
            result2.put(a.getBmName(), a.getAvgResult());
        }

        perfDifference.put("", "Perf diff(%)");

        Set<String> keys = null;

        if (result2.size() > result1.size())
            keys = result1.keySet();
        else
            keys = result2.keySet();


        for (String k : keys) {

            double val1 = result1.getOrDefault(k, 0.0);
            double val2 = result2.getOrDefault(k, 0.0);

            if (val1 != 0.0 && val2 != 0.0) {

                if (getLowerHigher(app_name.trim().toLowerCase()).equals("HIGHER")) {
                    if (Double.compare(val1, val2) < 0) {
                        double d = (val2 - val1) / Math.abs(val1);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "+" + percentage + "%");
                    } else if (Double.compare(val1, val2) > 0) {
                        double d = Math.abs(val2 - val1) / Math.abs(val1);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "-" + percentage + "%");
                    } else {
                        perfDifference.put(k, 0 + "%");
                    }
                    comment = "Higher is better";
                } else if (getLowerHigher(app_name.trim().toLowerCase()).equals("LOWER")){
                    if (Double.compare(val1, val2) > 0) {
                        double d = (val1 - val2) / Math.abs(val2);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "+" + percentage + "%");
                    } else if (Double.compare(val1, val2) < 0) {
                        double d = Math.abs(val1 - val2) / Math.abs(val2);
                        double percentage = util.round(d * 100, 2);
                        perfDifference.put(k, "-" + percentage + "%");
                    } else {
                        perfDifference.put(k, 0 + "%");
                    }
                    comment = "Lower is better";
                }
            }
        }

        Map<String, String> res1 = new LinkedHashMap<>();
        Map<String, String> res2 = new LinkedHashMap<>();

        res1.put("", cpu1 + "_" + type1);
        res2.put("", cpu2 + "_" + type2);

        for (String k : keys) {
            res1.put(k, result1.getOrDefault(k, 0.0).toString());
            res2.put(k, result2.getOrDefault(k, 0.0).toString());
        }

        List<Map<String, String>> dataSets = new ArrayList<>();
        dataSets.add(res1);
        dataSets.add(res2);
        dataSets.add(perfDifference);

        compareResult = CompareResult.builder().appName(app_name).bmName(bms).resultData(dataSets).comment(comment).build();

        return compareResult;

    }

    public static List<HeatMap> filterList(List<HeatMap> list, Set<String> bmsList) {
        for (int i = 0; i < list.size(); i++) {
            if (bmsList.contains(list.get(i).getBmName())) {
                continue;
            } else {
                list.remove(i);
                i--;
            }

        }
        return list;

    }

    public static Set<String> filterBmList(List<HeatMap> list1, List<HeatMap> list2) {
        Set<String> bmsList = new LinkedHashSet<>();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if (list1.get(i).getBmName().equals(list2.get(j).getBmName())) {
                    bmsList.add(list1.get(i).getBmName());
                }
            }
        }
        return bmsList;
    }

    public static List<List<HeatMap>> filterLists(List<HeatMap> list1, List<HeatMap> list2, List<HeatMap> list3, List<HeatMap> list4) {
        List<List<HeatMap>> filteredResult = new ArrayList<>();
        Set<String> bmsList1 = filterBmList(list1, list2);
        Set<String> bmsList2 = filterBmList(list1, list3);
        Set<String> bmsList3 = filterBmList(list1, list4);

        bmsList2.retainAll(bmsList1);

        if (bmsList2.size() > 0) {
            bmsList1.retainAll(bmsList2);
        }
        if (bmsList3.size() > 0) {
            bmsList3.retainAll(bmsList2);
            bmsList1.retainAll(bmsList3);
            bmsList2.retainAll(bmsList3);
        }
        Set<String> bmsList = bmsList1;


        list1 = filterList(list1, bmsList);
        list2 = filterList(list2, bmsList);
        list3 = filterList(list3, bmsList);
        list4 = filterList(list4, bmsList);

        filteredResult.add(list1);
        filteredResult.add(list2);
        filteredResult.add(list3);
        filteredResult.add(list4);

        return filteredResult;
    }

    public static List<List<HeatMap>> filterLists(List<HeatMap> list1, List<HeatMap> list2) {
        List<List<HeatMap>> filteredResult = new ArrayList<>();
        Set<String> bmsList = filterBmList(list1, list2);


        list1 = filterList(list1, bmsList);
        list2 = filterList(list2, bmsList);


        filteredResult.add(list1);
        filteredResult.add(list2);

        return filteredResult;
    }


    private void getHeatMapNodeResult(String cpu1, String type1, String cpu2, String type2, String cpu3, String type3, String cpu4, String type4, LinkedHashSet<String> category, Map<String, Double> bmResList1, Map<String, Double> bmResList2, Map<String, PerCore> perCoreListFirst, Map<String, PerCore> perCoreListSecond, Map<String, Double> perDollarFirst, Map<String, Double> perDollarSecond, Map<String, Double> perWattFirst, Map<String, Double> perWattSecond, List<Category> categories, String isvSelected) {

        List<List<HeatMap>> filteredLists = null;
        for (String cat : category) {

            Category category1 = new Category();
            category1.setCategory(cat);

            double catAvg = 0;
            double appAvg = 0;

            double perCoreCatAvg = 0;
            double perCoreAppAvg = 0;

            double perDollarCatAvg = 0;
            double perDollarAppAvg = 0;

            double perWattCatAvg = 0;
            double perWattAppAvg = 0;

            List<HeatMap> list5;
            List<HeatMap> list6;
            List<HeatMap> list7;
            List<HeatMap> list8;

            if(isvSelected.equals("All")) {
                list5 = heatMapService.getHeatMapData(cpu1, type1, cat);
                list6 = heatMapService.getHeatMapData(cpu2, type2, cat);
                list7 = heatMapService.getHeatMapData(cpu3, type3, cat);
                list8 = heatMapService.getHeatMapData(cpu4, type4, cat);
            }
            else {
                list5 = heatMapService.getHeatMapDataISV(cpu1, type1, isvSelected, cat);
                list6 = heatMapService.getHeatMapDataISV(cpu2, type2, isvSelected, cat);
                list7 = heatMapService.getHeatMapDataISV(cpu3, type3, isvSelected, cat);
                list8 = heatMapService.getHeatMapDataISV(cpu4, type4, isvSelected, cat);
            }

            filteredLists = filterLists(list5, list6, list7, list8);


            list5 = filteredLists.get(0);
            list6 = filteredLists.get(1);

            Set<String> isv = new LinkedHashSet<>();

            for (HeatMap heatMap : list5) {
                if (cat.contains(heatMap.getCategory())) {
                    isv.add(heatMap.getIsv());
                }
            }

            if (isv.size() < 1) {
                continue;
            }

            Set<ISV> isvList = new LinkedHashSet<>();

            double aAvg = 0;
            int aCount = 0;

            double perCoreAAvg = 0;
            int perCoreACount = 0;

            double perDollarAAvg = 0;
            int perDollarACount = 0;

            double perWattAAvg = 0;
            int perWattACount = 0;


            for (String i : isv) {
                ISV isv1 = new ISV();

                isv1.setIsv(i);


                Set<String> appName = new LinkedHashSet<>();
                for (HeatMap heatMap : list5) {
                    if (i.contains(heatMap.getIsv())) {
                        appName.add(heatMap.getAppName());
                    }
                }

                if (appName.size() < 1) {
                    continue;
                }

                Set<App> appList = new LinkedHashSet<>();

                for (String a : appName) {
                    App app = new App();
                    app.setApplication(chartRestController.getAppName(a));

                    Set<String> bmName = new LinkedHashSet<>();
                    for (HeatMap heatMap : list5) {
                        if (a.contains(heatMap.getAppName())) {
                            bmName.add(heatMap.getBmName());
                        }
                    }

                    if (bmName.size() < 1) {
                        continue;
                    }


                    double bAvg = 0;
                    int bCount = 0;

                    double perCoreBAvg = 0;
                    int perCoreBCount = 0;


                    double perDollarBAvg = 0;
                    int perDollarBCount = 0;


                    double perWattBAvg = 0;
                    int perWattBCount = 0;

                    Map<String, Double> bmUplift = new LinkedHashMap<>();
                    Map<String, Double> perCoreBmUplift = new LinkedHashMap<>();
                    Map<String, Double> perDollarBmUplift = new LinkedHashMap<>();
                    Map<String, Double> perWattBmUplift = new LinkedHashMap<>();

                    for (String b : bmName) {

                        double val1 = bmResList1.getOrDefault(b, 0.0);
                        double val2 = bmResList2.getOrDefault(b, 0.0);

                        double d = 0;
                        double percentage = 0;
                        int flag = 0;
                        if (val1 != 0.0 && val2 != 0.0) {

                            if (getLowerHigher(a.trim().toLowerCase()).equals("HIGHER")) {
                                if (Double.compare(val1, val2) < 0) {
                                    flag = 0;
                                    d = (val2 - val1) / Math.abs(val1); //+
                                    percentage = util.round(d * 100, 2);

                                } else if (Double.compare(val1, val2) > 0) {
                                    flag = 1;
                                    d = Math.abs(val2 - val1) / Math.abs(val1);
                                    percentage = util.round(d * 100, 2);

                                } else {
                                    percentage = 0;
                                }

                            } else if (getLowerHigher(a.trim().toLowerCase()).equals("LOWER")) {
                                if (Double.compare(val1, val2) > 0) {
                                    flag = 0;
                                    d = (val1 - val2) / Math.abs(val2);
                                    percentage = util.round(d * 100, 2); //+
                                } else if (Double.compare(val1, val2) < 0) {
                                    flag = 1;
                                    d = Math.abs(val1 - val2) / Math.abs(val2);
                                    percentage = util.round(d * 100, 2);  //-
                                } else {
                                    percentage = 0;
                                }

                            }
                        }

                        if (flag == 1) {
                            percentage = -1.0 * percentage;
                        }

                        bAvg += percentage;
                        bCount++;
                        bmUplift.put(b, percentage);

                    }


                    for (String b : bmName) {

                        PerCore v1 = perCoreListFirst.getOrDefault(b, new PerCore());

                        PerCore v2 = perCoreListSecond.getOrDefault(b, new PerCore());
                        double d = 0;
                        double percentage = 0;
                        int flag = 0;
                        double d1 = 0;
                        double d2 = 0;
                        int core1 = Integer.valueOf(v1.getCores());
                        int core2 = Integer.valueOf(v2.getCores());

                        double val1 = v1.getResult();
                        double val2 = v2.getResult();


                        if (v1.getResult() != 0.0 && v2.getResult() != 0.0) {

                            if (getLowerHigher(a.trim().toLowerCase()).equals("HIGHER")) {

                                d1 = val2 / val1;
                                d2 = (double) core1 / core2;
                                d = d1 * d2;
                                d = d - 1;

                            } else if (getLowerHigher(a.trim().toLowerCase()).equals("LOWER")) {

                                d1 = val1 / val2;
                                d2 = (double) core1 / core2;
                                d = d1 * d2;
                                d = d - 1;
                            }

                            percentage = (util.round(d * 100, 2));
                        }

                        if (flag == 1) {
                            percentage = -1.0 * percentage;
                        }

                        perCoreBAvg += percentage;
                        perCoreBCount++;
                        perCoreBmUplift.put(b, percentage);

                    }


                    for (String b : bmName) {

                        double val1 = perDollarFirst.getOrDefault(b, 0.0);
                        double val2 = perDollarSecond.getOrDefault(b, 0.0);

                        double d = 0;
                        double percentage = 0;
                        int flag = 0;
                        if (val1 != 0.0 && val2 != 0.0) {


                            if (Double.compare(val1, val2) < 0) {
                                flag = 0;
                                d = (val2 - val1) / Math.abs(val1); //+
                                percentage = util.round(d * 100, 2);

                            } else if (Double.compare(val1, val2) > 0) {
                                flag = 1;
                                d = Math.abs(val2 - val1) / Math.abs(val1);
                                percentage = util.round(d * 100, 2);

                            } else {
                                percentage = 0;
                            }

                        }

                        if (flag == 1) {
                            percentage = -1.0 * percentage;
                        }

                        perDollarBAvg += percentage;
                        perDollarBCount++;
                        perDollarBmUplift.put(b, percentage);

                    }


                    for (String b : bmName) {

                        double val1 = perWattFirst.getOrDefault(b, 0.0);
                        double val2 = perWattSecond.getOrDefault(b, 0.0);

                        double d = 0;
                        double percentage = 0;
                        int flag = 0;
                        if (val1 != 0.0 && val2 != 0.0) {

                            if (Double.compare(val1, val2) < 0) {
                                flag = 0;
                                d = (val2 - val1) / Math.abs(val1); //+
                                percentage = util.round(d * 100, 2);

                            } else if (Double.compare(val1, val2) > 0) {
                                flag = 1;
                                d = Math.abs(val2 - val1) / Math.abs(val1);
                                percentage = util.round(d * 100, 2);

                            } else {
                                percentage = 0;
                            }


                        }

                        if (flag == 1) {
                            percentage = -1.0 * percentage;
                        }

                        perWattBAvg += percentage;
                        perWattBCount++;
                        perWattBmUplift.put(b, percentage);

                    }

                    app.setBmUplift(bmUplift);
                    app.setPerCoreBmUplift(perCoreBmUplift);
                    app.setPerDollarBmUplift(perDollarBmUplift);
                    app.setPerWattBmUplift(perWattBmUplift);


                    appAvg = util.round(bAvg / bCount, 2);

                    perCoreAppAvg = util.round(perCoreBAvg / perCoreBCount, 2);

                    perDollarAppAvg = util.round(perDollarBAvg / perDollarBCount, 2);

                    perWattAppAvg = util.round(perWattBAvg / perWattBCount, 2);

                    app.setUplift(appAvg);
                    app.setPer_Core_Uplift(perCoreAppAvg);

                    app.setPer_Dollar_Uplift(perDollarAppAvg);
                    app.setPer_Watt_Uplift(perWattAppAvg);

                    aAvg += appAvg;
                    aCount++;

                    perCoreAAvg += perCoreAppAvg;
                    perCoreACount++;

                    perDollarAAvg += perDollarAppAvg;
                    perDollarACount++;


                    perWattAAvg += perWattAppAvg;
                    perWattACount++;

                    appList.add(app);
                }


                catAvg = util.round(aAvg / aCount, 2);
                perCoreCatAvg = util.round(perCoreAAvg / perCoreACount, 2);

                perDollarCatAvg = util.round(perDollarAAvg / perDollarACount, 2);
                perWattCatAvg = util.round(perWattAAvg / perWattACount, 2);

                isv1.setApp(appList);
                isvList.add(isv1);
            }
            category1.setIsvList(isvList);
            category1.setUplift(catAvg);
            category1.setPer_Core_Uplift(perCoreCatAvg);
            category1.setPer_Dollar_Uplift(perDollarCatAvg);
            category1.setPer_Watt_Uplift(perWattCatAvg);
            categories.add(category1);
        }

    }

    private List<HeatMapResult> getConsolidatedResult(List<Category> categories, List<HeatMapResult> resList) {

        HeatMapResult h;
        HashMap<String, Double> perCorePercentage = new HashMap<>();
        HashMap<String, Double> perDollarPercentage = new HashMap<>();
        HashMap<String, Double> perWattPercentage = new HashMap<>();

        for (Category c : categories) {
            h = new HeatMapResult();

            h.setCategory(c.getCategory());


            if (c.getUplift() > 0.0) {
                h.setPerNode1("+" + String.valueOf(c.getUplift()) + "%");
            } else {
                h.setPerNode1(String.valueOf(c.getUplift()) + "%");
            }

            if (c.getPer_Core_Uplift() > 0.0) {
                h.setPerCore1("+" + String.valueOf(c.getPer_Core_Uplift()) + "%");
            } else {
                h.setPerCore1(String.valueOf(c.getPer_Core_Uplift()) + "%");
            }

            if (c.getPer_Dollar_Uplift() > 0.0) {
                h.setPerDollar1("+" + String.valueOf(c.getPer_Dollar_Uplift()) + "%");
            } else {
                h.setPerDollar1(String.valueOf(c.getPer_Dollar_Uplift()) + "%");
            }


            if (c.getPer_Watt_Uplift() > 0.0) {
                h.setPerWatt1("+" + String.valueOf(c.getPer_Watt_Uplift()) + "%");
            } else {
                h.setPerWatt1(String.valueOf(c.getPer_Watt_Uplift()) + "%");
            }

            resList.add(h);

            for (ISV i : c.getIsvList()) {

                for (App a : i.getApp()) {
                    h = new HeatMapResult();
                    h.setISV(i.getIsv());
                    h.setApplication(a.getApplication());

                    if (a.getUplift() > 0.0) {
                        h.setPerNode1("+" + String.valueOf(a.getUplift()) + "%");
                    } else {
                        h.setPerNode1(String.valueOf(a.getUplift()) + "%");
                    }


                    if (a.getPer_Core_Uplift() > 0.0) {
                        h.setPerCore1("+" + String.valueOf(a.getPer_Core_Uplift()) + "%");
                    } else {
                        h.setPerCore1(String.valueOf(a.getPer_Core_Uplift()) + "%");
                    }


                    if (a.getPer_Dollar_Uplift() > 0.0) {
                        h.setPerDollar1("+" + String.valueOf(a.getPer_Dollar_Uplift()) + "%");
                    } else {
                        h.setPerDollar1(String.valueOf(a.getPer_Dollar_Uplift()) + "%");
                    }


                    if (a.getPer_Watt_Uplift() > 0.0) {
                        h.setPerWatt1("+" + String.valueOf(a.getPer_Watt_Uplift()) + "%");
                    } else {
                        h.setPerWatt1(String.valueOf(a.getPer_Watt_Uplift()) + "%");
                    }


                    resList.add(h);


                    for (Map.Entry<String, Double> b : a.getPerCoreBmUplift().entrySet()) {
                        perCorePercentage.put(b.getKey(), b.getValue());
                    }

                    for (Map.Entry<String, Double> b : a.getPerDollarBmUplift().entrySet()) {
                        perDollarPercentage.put(b.getKey(), b.getValue());
                    }

                    for (Map.Entry<String, Double> b : a.getPerWattBmUplift().entrySet()) {
                        perWattPercentage.put(b.getKey(), b.getValue());
                    }


                    for (Map.Entry<String, Double> b : a.getBmUplift().entrySet()) {
                        h = new HeatMapResult();
                        h.setBenchmark(b.getKey());

                        double perCoreUplift = perCorePercentage.get(b.getKey());

                        double perDollarUplift = perDollarPercentage.get(b.getKey());

                        double perWattUplift = perWattPercentage.get(b.getKey());

                        if (b.getValue() > 0.0) {
                            h.setPerNode1("+" + String.valueOf(b.getValue()) + "%");
                        } else {

                            h.setPerNode1(String.valueOf(b.getValue()) + "%");
                        }

                        if (perCoreUplift > 0.0) {
                            h.setPerCore1("+" + String.valueOf(perCoreUplift) + "%");
                        } else {
                            h.setPerCore1(String.valueOf(perCoreUplift) + "%");
                        }

                        if (perDollarUplift > 0.0) {
                            h.setPerDollar1("+" + String.valueOf(perDollarUplift) + "%");
                        } else {
                            h.setPerDollar1(String.valueOf(perDollarUplift) + "%");
                        }

                        if (perWattUplift > 0.0) {
                            h.setPerWatt1("+" + String.valueOf(perWattUplift) + "%");
                        } else {
                            h.setPerWatt1(String.valueOf(perWattUplift) + "%");
                        }


                        resList.add(h);
                    }
                }

            }

        }
        return resList;
    }

    @GetMapping("/isvDrop")
    public LinkedHashSet<String>  getISVDropDown(String[] cpuList, String[] typeList, String[] workloads) {
        LinkedHashSet<String>  isvs = new LinkedHashSet<>();

        String cpu1 = cpuList[0];
        String cpu2 = cpuList[1];
        String type1 = typeList[0];
        String type2 = typeList[1];
        String cpu3 = null;
        String cpu4 = null;
        String type3 = null;
        String type4 = null;
        List<HeatMap> list3 = new ArrayList<>();
        List<HeatMap> list4 = new ArrayList<>();

        if (cpuList.length > 2 && typeList.length > 2) {
            cpu3 = cpuList[2];
            type3 = typeList[2];
            list3 = heatMapService.getHeatMapData(cpu3, type3);

        }
        if (cpuList.length > 3 && typeList.length > 3) {
            cpu4 = cpuList[3];
            type4 = typeList[3];
            list4 = heatMapService.getHeatMapData(cpu4, type4);
        }

        List<HeatMap> list1 = heatMapService.getHeatMapData(cpu1, type1);
        List<HeatMap> list2 = heatMapService.getHeatMapData(cpu2, type2);

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0)
            return isvs;

        List<List<HeatMap>> filteredLists = null;

        filteredLists = filterLists(list1, list2, list3, list4);

        list1 = filteredLists.get(0);

        isvs = list1.stream()
                .map(HeatMap::getIsv)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return isvs;
    }

    @GetMapping("/heatMap")
    public HeatMapOutput getHeatMapData(String[] cpuList, String[] typeList, String isv) {
        HeatMapOutput heatMapOutput = new HeatMapOutput();
        List<Category> categories = new ArrayList<>();
        List<HeatMapResult> resList = new ArrayList<>();
        String cpu1 = cpuList[0];
        String cpu2 = cpuList[1];
        String type1 = typeList[0];
        String type2 = typeList[1];
        String cpu3 = null;
        String cpu4 = null;
        String type3 = null;
        String type4 = null;
        List<HeatMap> list1;
        List<HeatMap> list2;
        List<HeatMap> list3 = new ArrayList<>();
        List<HeatMap> list4 = new ArrayList<>();

        if (cpuList.length > 2 && typeList.length > 2) {
            cpu3 = cpuList[2];
            type3 = typeList[2];
            list3 = heatMapService.getHeatMapData(cpu3, type3);

        }
        if (cpuList.length > 3 && typeList.length > 3) {
            cpu4 = cpuList[3];
            type4 = typeList[3];
            list4 = heatMapService.getHeatMapData(cpu4, type4);
        }

        if(isv.equals("All")) {
            list1 = heatMapService.getHeatMapData(cpu1, type1);
            list2 = heatMapService.getHeatMapData(cpu2, type2);
        }
        else {
            list1 = heatMapService.getHeatMapDataISV(cpu1, type1, isv);
            list2 = heatMapService.getHeatMapDataISV(cpu2, type2, isv);
        }


        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0)
            return heatMapOutput;

        Set<String> bmsList = new LinkedHashSet<>();

        List<List<HeatMap>> filteredLists = null;

        filteredLists = filterLists(list1, list2, list3, list4);

        list1 = filteredLists.get(0);
        list2 = filteredLists.get(1);
        list3 = filteredLists.get(2);
        list4 = filteredLists.get(3);


        LinkedHashSet<String> category = list1.stream()
                .map(HeatMap::getCategory)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (list1 == null || list1.size() == 0 || list2 == null || list2.size() == 0)
            return heatMapOutput;

        Map<String, Double> bmResList1 = new LinkedHashMap<>();
        Map<String, Double> bmResList2 = new LinkedHashMap<>();
        Map<String, Double> bmResList3 = new LinkedHashMap<>();
        Map<String, Double> bmResList4 = new LinkedHashMap<>();


        Map<String, PerCore> perCoreListFirst = new LinkedHashMap<>();

        Map<String, PerCore> perCoreListSecond = new LinkedHashMap<>();

        Map<String, PerCore> perCoreListThird = new LinkedHashMap<>();

        Map<String, PerCore> perCoreListFourth = new LinkedHashMap<>();


        Map<String, Double> perDollarListFirst = new LinkedHashMap<>();

        Map<String, Double> perDollarListSecond = new LinkedHashMap<>();

        Map<String, Double> perDollarListThird = new LinkedHashMap<>();

        Map<String, Double> perDollarListFourth = new LinkedHashMap<>();


        Map<String, Double> perWattListFirst = new LinkedHashMap<>();

        Map<String, Double> perWattListSecond = new LinkedHashMap<>();

        Map<String, Double> perWattListThird = new LinkedHashMap<>();

        Map<String, Double> perWattListFourth = new LinkedHashMap<>();


        for (HeatMap h : list1) {

            bmResList1.put(h.getBmName(), h.getAvgResult());
            perCoreListFirst.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "baseline"));

            double perfPerDollar = util.PerfPerDollar(h.getCpuSku(), h.getAvgResult(), h.getAppName());

            perDollarListFirst.put(h.getBmName(), perfPerDollar);

            double perfPerWatt = util.PerfPerWatt(h.getCpuSku(), h.getAvgResult(), h.getAppName());
            perWattListFirst.put(h.getBmName(), perfPerWatt);
        }


        for (HeatMap h : list2) {

            bmResList2.put(h.getBmName(), h.getAvgResult());
            perCoreListSecond.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "comparative"));

            double perfPerDollar = util.PerfPerDollar(h.getCpuSku(), h.getAvgResult(), h.getAppName());
            perDollarListSecond.put(h.getBmName(), perfPerDollar);

            double perfPerWatt = util.PerfPerWatt(h.getCpuSku(), h.getAvgResult(), h.getAppName());
            perWattListSecond.put(h.getBmName(), perfPerWatt);
        }

        for (HeatMap h : list3) {

            bmResList3.put(h.getBmName(), h.getAvgResult());
            perCoreListThird.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "comparative"));
            double perfPerDollar = util.PerfPerDollar(h.getCpuSku(), h.getAvgResult(), h.getAppName());
            perDollarListThird.put(h.getBmName(), perfPerDollar);

            double perfPerWatt = util.PerfPerWatt(h.getCpuSku(), h.getAvgResult(), h.getAppName());
            perWattListThird.put(h.getBmName(), perfPerWatt);

        }

        for (HeatMap h : list4) {

            bmResList4.put(h.getBmName(), h.getAvgResult());
            perCoreListFourth.put(h.getBmName(), new PerCore(h.getCores(), h.getAvgResult(), "comparative"));
            double perfPerDollar = util.PerfPerDollar(h.getCpuSku(), h.getAvgResult(), h.getAppName());
            perDollarListFourth.put(h.getBmName(), perfPerDollar);

            double perfPerWatt = util.PerfPerWatt(h.getCpuSku(), h.getAvgResult(), h.getAppName());
            perWattListFourth.put(h.getBmName(), perfPerWatt);
        }

        List<Category> categories1 = new ArrayList<>();
        List<Category> categories2 = new ArrayList<>();
        List<HeatMapResult> resList1 = new ArrayList<>();
        List<HeatMapResult> resList2 = new ArrayList<>();


        getHeatMapNodeResult(cpu1, type1, cpu2, type2, cpu3, type3, cpu4, type4, category, bmResList1, bmResList2, perCoreListFirst, perCoreListSecond, perDollarListFirst, perDollarListSecond, perWattListFirst, perWattListSecond, categories, isv);
        getHeatMapNodeResult(cpu1, type1, cpu3, type3, cpu2, type2, cpu4, type4, category, bmResList1, bmResList3, perCoreListFirst, perCoreListThird, perDollarListFirst, perDollarListThird, perWattListFirst, perWattListThird, categories1, isv);
        getHeatMapNodeResult(cpu1, type1, cpu4, type4, cpu2, type2, cpu3, type3, category, bmResList1, bmResList4, perCoreListFirst, perCoreListFourth, perDollarListFirst, perDollarListFourth, perWattListFirst, perWattListFourth, categories2, isv);

        resList = getConsolidatedResult(categories, resList);
        resList1 = getConsolidatedResult(categories1, resList1);
        resList2 = getConsolidatedResult(categories2, resList2);


        for (int i = 0; i < resList.size(); i++) {

            HeatMapResult h = resList.get(i);
            HeatMapResult h1 = new HeatMapResult();
            HeatMapResult h2 = new HeatMapResult();
            if (cpuList.length > 2 && typeList.length > 2 && resList1.size() > 0) {
                h1 = resList1.get(i);
            }
            if (cpuList.length > 3 && typeList.length > 3 && resList2.size() > 0) {
                h2 = resList2.get(i);
            }

            if (cpuList.length > 2 && typeList.length > 2) {
                h.setPerCore2(h1.getPerCore1());
                h.setPerNode2(h1.getPerNode1());
                h.setPerDollar2(h1.getPerDollar1());
                h.setPerWatt2(h1.getPerWatt1());
            }

            if (cpuList.length > 3 && typeList.length > 3) {
                h.setPerCore3(h2.getPerCore1());
                h.setPerNode3(h2.getPerNode1());
                h.setPerDollar3(h2.getPerDollar1());
                h.setPerWatt3(h2.getPerWatt1());
            }
        }

        heatMapOutput.setHeatMapResults(resList);

        List<String> columns = new ArrayList<>();

        columns.add("category");
        columns.add("isv");
        columns.add("application");
        columns.add("benchmark");
        columns.add("perNode1");


        double cpuPrice1 = util.getCpuPrice(cpu1.trim());
        double cpuPrice2 = util.getCpuPrice(cpu2.trim());
        double cpuPrice3 = 0.0;
        double cpuPrice4 = 0.0;

        int cpuWatt1 = util.getCpuTDP(cpu1.trim());
        int cpuWatt2 = util.getCpuTDP(cpu2.trim());
        int cpuWatt3 = 0;
        int cpuWatt4 = 0;

        if (cpuList.length > 2 && typeList.length > 2) {
            cpuPrice3 = util.getCpuPrice(cpu3.trim());
            cpuWatt3 = util.getCpuTDP(cpu3.trim());
        }
        if (cpuList.length > 3 && typeList.length > 3) {
            cpuPrice4 = util.getCpuPrice(cpu4.trim());
            cpuWatt4 = util.getCpuTDP(cpu4.trim());
        }

        if (cpuList.length > 2 && typeList.length > 2 && resList1.size() > 0)
            columns.add("perNode2");

        if (cpuList.length > 3 && typeList.length > 3 && resList2.size() > 0)
            columns.add("perNode3");

        if (cpuList.length > 2 && typeList.length > 2)
            columns.add("");

        columns.add("perCore1");

        if (cpuList.length > 2 && typeList.length > 2 && resList1.size() > 0)
            columns.add("perCore2");

        if (cpuList.length > 3 && typeList.length > 3 && resList2.size() > 0)
            columns.add("perCore3");

        if (Double.compare(cpuPrice1, 0.0) > 0 && (Double.compare(cpuPrice2, 0.0) > 0 || Double.compare(cpuPrice3, 0.0) > 0 || Double.compare(cpuPrice4, 0.0) > 0)) {

            if (cpuList.length > 2 && typeList.length > 2)
                columns.add("");

            if (Double.compare(cpuPrice2, 0.0) > 0)
                columns.add("perDollar1");

            if (cpuList.length > 2 && typeList.length > 2 && Double.compare(cpuPrice3, 0.0) > 0 && resList1.size() > 0)
                columns.add("perDollar2");

            if (cpuList.length > 3 && typeList.length > 3 && Double.compare(cpuPrice4, 0.0) > 0 && resList2.size() > 0)
                columns.add("perDollar3");

        }

        if (cpuWatt1 > 0 && (cpuWatt2 > 0 || cpuWatt3 > 0 || cpuWatt4 > 0)) {

            if (cpuList.length > 2 && typeList.length > 2)
                columns.add("");

            if (cpuWatt2 > 0)
                columns.add("perWatt1");

            if (cpuList.length > 2 && typeList.length > 2 && cpuWatt3 > 0 && resList1.size() > 0)
                columns.add("perWatt2");

            if (cpuList.length > 3 && typeList.length > 3 && cpuWatt4 > 0 && resList2.size() > 0)
                columns.add("perWatt3");
        }

        heatMapOutput.setColumns(columns);

        return heatMapOutput;
    }


}
