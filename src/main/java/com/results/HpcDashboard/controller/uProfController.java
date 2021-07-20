package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.uProf.UProfDataset;
import com.results.HpcDashboard.dto.uProf.UProfOutput;
import com.results.HpcDashboard.dto.multichart.Dataset;
import com.results.HpcDashboard.models.*;
import com.results.HpcDashboard.repo.AppMapRepo;
import com.results.HpcDashboard.repo.UProfCalculatedRepo;
import com.results.HpcDashboard.repo.UProfRawRepo;
import com.results.HpcDashboard.repo.UProfReferenceRepo;
import com.results.HpcDashboard.util.Util;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController

public class uProfController {

    @Autowired
    UProfRawRepo uProfRawRepo;

    @Autowired
    UProfCalculatedRepo uProfCalculatedRepo;

    @Autowired
    UProfReferenceRepo uProfReferenceRepo;

    @Autowired
    Util util;

    @Autowired
    AppMapRepo appMapRepo;

    @Autowired
    Environment environment;


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

    @PostMapping(value = "/uProfReferenceJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertuProfReference(@RequestBody List<UProfReference> uProfReferences) {
        if(uProfReferences != null || uProfReferences.size() > 0 ) {
            try {
                uProfReferenceRepo.saveAll(uProfReferences);
            } catch (Exception e) {
                return new ResponseEntity(ExceptionUtils.getRootCauseMessage(e) +"\n" , HttpStatus.OK);
            }
        }
        return new ResponseEntity("Success! \n",HttpStatus.OK);
    }


    @PostMapping("/uProfResult")
    public ResponseEntity<String> insertuProfResult( @RequestParam("file") MultipartFile file, @RequestParam("runType") String runType) throws Exception{

        String fileName = org.apache.commons.io.FilenameUtils.getName(file.getOriginalFilename()).split("\\.")[0];
        List<List<String>> list = new ArrayList<>();
        List<List<String>> intList = new ArrayList<>();
        List<List<String>> intList1 = new ArrayList<>();
        List<List<String>> intList2 = new ArrayList<>();
        List<List<String>> intList3 = new ArrayList<>();
        List<List<String>> finalList = new ArrayList<>();

            try {
                BufferedReader fileReader = new BufferedReader(new
                        InputStreamReader(file.getInputStream(), "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);

                Iterable<CSVRecord> csvRecords = csvParser.getRecords();

                Iterator<CSVRecord> iterator = csvRecords.iterator();

                long skipLines=0;

                for (CSVRecord record : csvRecords) {

                    if(record.get(0).equals("Core-0"))
                        skipLines= record.getRecordNumber();

                }

                for (long i = 0; i < skipLines-1; i++) {
                    if (iterator.hasNext()) {
                        iterator.next();
                    }
                }

                while (iterator.hasNext()) {
                    CSVRecord record = iterator.next();

                    List<String> l = new ArrayList<>();

                    for(int i=0 ; i< record.size();i++)
                    {
                        l.add(record.get(i));
                    }

                    list.add(l);
                }

                ArrayList<String> header = new ArrayList<String>();
                String start="";
                for (int i = 0; i < list.get(0).size(); i++) {

                    if(!list.get(0).get(i).equals(""))
                        start = list.get(0).get(i);


                    String end = list.get(1).get(i).replace(" ","_").split("\\(")[0].replace("-","_").replace("/","_").replace("%","percent");

                    if(end.endsWith("_"))
                        end = end.substring(0,end.length()-1);

                    header.add(start.replace("-","_") + "_" + end);
                }

                intList.add(header);


                for (int i=2;i<list.size();i++)
                {
                    intList.add(list.get(i));
                }

                int core1 = intList.get(0).indexOf("Core_1_Utilization");

                int ccx0Start = intList.get(0).indexOf("CCX_0_L3_Access");



                for(int i=0; i<intList.size();i++)
                {

                    intList.get(i).subList(core1, ccx0Start).clear();

                    intList1.add(intList.get(i));
                }

                int ccx1End = intList.get(0).indexOf("CCX_1_Ave_L3_Miss_Latency");

                int pkg0 = intList.get(0).indexOf("Package_0_Total_Mem_Bw");


                for(int i=0; i<intList1.size();i++)
                {

                    intList1.get(i).subList(ccx1End+1,pkg0).clear();

                    finalList.add(intList.get(i));
                }


                List<UProfRaw> uProfRawList = util.generateUProfRaw(finalList,fileName, runType);

                List<Double> Cpu_Utilization = uProfRawList.stream().map(UProfRaw::getCore_0_Utilization).collect(Collectors.toList());
                List<Double> Cpu_Eff_Freq = uProfRawList.stream().map(UProfRaw::getCore_0_Eff_Freq).collect(Collectors.toList());
                List<Double> IPC = uProfRawList.stream().map(UProfRaw::getCore_0_IPC).collect(Collectors.toList());
                List<Double> Retired_SSE_AVX_Flops = uProfRawList.stream().map(UProfRaw::getCore_0_Retired_SSE_AVX_Flops).collect(Collectors.toList());
                List<Double> L3_Miss = uProfRawList.stream().map(UProfRaw::getCCX_0_L3_Miss_percent).collect(Collectors.toList());
                List<Double> Total_Mem_Bw = uProfRawList.stream().map(UProfRaw::getPackage_0_Total_Mem_Bw).collect(Collectors.toList());
                List<Double> Total_Mem_RdBw = uProfRawList.stream().map(UProfRaw::getPackage_0_Total_Mem_RdBw).collect(Collectors.toList());
                List<Double> Total_Mem_WrBw = uProfRawList.stream().map(UProfRaw::getPackage_0_Total_Mem_WrBw).collect(Collectors.toList());
                List<Double> Total_xGMI0_BW = uProfRawList.stream().map(UProfRaw::getPackage_0_Approximate_xGMI_outbound_data_bytes).collect(Collectors.toList());

                uProfRawRepo.saveAll(uProfRawList);

                UProfCalculated uProfCalculated = UProfCalculated.builder().procAppBm(fileName).processor(fileName.split("_")[0]) .runType(runType).Cpu_Utilization(util.getuProfAverage(Cpu_Utilization)).Cpu_Eff_Freq(util.getuProfAverage(Cpu_Eff_Freq)).IPC(util.getuProfAverage(IPC)).Retired_SSE_AVX_Flops(util.getuProfAverage(Retired_SSE_AVX_Flops)).L3_Hit(100-util.getuProfAverage(L3_Miss)) .Total_Mem_Bw(util.getuProfAverage(Total_Mem_Bw)).Total_Mem_RdBw(util.getuProfAverage(Total_Mem_RdBw)).Total_Mem_WrBw(util.getuProfAverage(Total_Mem_WrBw)).Total_xGMI0_BW(util.getuProfAverage(Total_xGMI0_BW)).build();

                uProfCalculatedRepo.save(uProfCalculated);
            }

            catch (Exception ex) {
                System.out.println(ExceptionUtils.getStackTrace(ex));
                return new ResponseEntity(ExceptionUtils.getStackTrace(ex), HttpStatus.EXPECTATION_FAILED);
            }

        return new ResponseEntity("Success!", HttpStatus.OK);
    }


    @GetMapping("/uProfRadarChart")
    public UProfOutput getuProfData(String[] cpuList, String[] typeList) {

        System.out.println(environment.getProperty("workload.name"));
        UProfOutput uProfOutput = null;
        List<UProfCalculated> uProfCalculated = new ArrayList<>();
        List<UProfDataset> uProfDatasets = new ArrayList<>();
        String cpu1 = cpuList[0];
        String type1 = typeList[0];
        String cpu2 = null;
        String cpu3 = null;
        String cpu4 = null;
        String type2 = null;
        String type3 = null;
        String type4 = null;
        UProfCalculated list1;
        UProfCalculated list2 = new UProfCalculated();
        UProfCalculated list3 = new UProfCalculated();
        UProfCalculated list4 = new UProfCalculated();
        Set<String> bmsList = new LinkedHashSet<>();

        list1 = uProfCalculatedRepo.findUProf_Calculated(cpu1,type1);

        uProfCalculated.add(list1);
        bmsList.add(list1.getProcAppBm());


        if (cpuList.length > 1 && typeList.length > 1) {
            cpu2 = cpuList[1];
            type2 = typeList[1];
            list2 = uProfCalculatedRepo.findUProf_Calculated(cpu2,type2);
            bmsList.add(list2.getProcAppBm());
            uProfCalculated.add(list2);
        }

        if (cpuList.length > 2 && typeList.length > 2) {
            cpu3 = cpuList[2];
            type3 = typeList[2];
            list3 = uProfCalculatedRepo.findUProf_Calculated(cpu3,type3);
            bmsList.add(list3.getProcAppBm());
            uProfCalculated.add(list3);

        }
        if (cpuList.length > 3 && typeList.length > 3) {
            cpu4 = cpuList[3];
            type4 = typeList[3];
            list4 = uProfCalculatedRepo.findUProf_Calculated(cpu4,type4);
            bmsList.add(list4.getProcAppBm());
            uProfCalculated.add(list4);
        }


        if (list1 == null  || list2 == null )
            return uProfOutput;



        List<String> metrics = new ArrayList<>();
        metrics.add("CPU Utilization(%)");
        metrics.add("CPU Eff Freq");
        metrics.add("IPC (Sys + User)");
        metrics.add("Retired SSE/AVX Flops(GFLOPs)");
        metrics.add("Tot Mem BW");
        metrics.add("Tot Mem Read BW");
        metrics.add("Tot Mem Write BW");
        metrics.add("Tot xGMI BW");
        metrics.add("L3 Hit %");
        List<Integer> listItem = null;

        UProfDataset uProfDataset = null;
        for(UProfCalculated uProfCalculate : uProfCalculated){

            UProfReference uProfReference = uProfReferenceRepo.findUProf_Reference(uProfCalculate.getProcessor());

            listItem = new ArrayList<>();

            listItem.add((int) Math.round(uProfCalculate.getCpu_Utilization()/uProfReference.getCpu_Utilization() * 100));
            listItem.add((int) Math.round(uProfCalculate.getCpu_Eff_Freq()/uProfReference.getCpu_Eff_Freq() * 100)) ;
            listItem.add((int) Math.round(uProfCalculate.getIPC()/uProfReference.getIPC() * 100));
            listItem.add((int) Math.round(uProfCalculate.getRetired_SSE_AVX_Flops()/uProfReference.getRetired_SSE_AVX_Flops() * 100));
            listItem.add((int) Math.round(uProfCalculate.getTotal_Mem_Bw()/uProfReference.getTotal_Mem_Bw() * 100));
            listItem.add((int) Math.round(uProfCalculate.getTotal_Mem_RdBw()/uProfReference.getTotal_Mem_RdBw() * 100));
            listItem.add((int) Math.round(uProfCalculate.getTotal_Mem_WrBw()/uProfReference.getTotal_Mem_WrBw() * 100));
            listItem.add((int) Math.round(uProfCalculate.getTotal_xGMI0_BW()/uProfReference.getTotal_xGMI0_BW() * 100));
            listItem.add((int) Math.round(uProfCalculate.getL3_Hit()/uProfReference.getL3_Hit() * 100));
            uProfDataset = UProfDataset.builder().procAppBM(uProfCalculate.getProcAppBm()).value(listItem).build();
            uProfDatasets.add(uProfDataset);
        }

        uProfOutput = UProfOutput.builder().benchmarks(bmsList).metrics(metrics).dataset(uProfDatasets).build();

        return uProfOutput;
    }


}
