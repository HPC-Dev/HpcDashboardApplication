package com.results.HpcDashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.services.AverageResultService;
import com.results.HpcDashboard.services.ResultService;
import com.results.HpcDashboard.util.GenerateCSVReport;
import com.results.HpcDashboard.util.GenerateExcelReport;
import com.results.HpcDashboard.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ResultDashboardController {
    @Autowired
    ResultService resultService;

    @Autowired
    AverageResultService averageResultService;

    @Autowired
    Util util;

    @ModelAttribute("command")
    public FormCommand formCommand() {
        return new FormCommand();
    }

    @GetMapping("/result")
    public String showResultForm() {
        return "resultForm";
    }

    @GetMapping("/deleteJob")
    public String showDeleteJobForm() {
        return "deleteJob";
    }

    @PostMapping("/result")
    public String insertResult(
            @ModelAttribute("command") @Valid FormCommand command,
            Model model, @RequestParam("file") MultipartFile file, Errors errors, RedirectAttributes redirectAttributes) {

        if(command.getRadioButtonSelectedValue().equals("paste")) {
            String[] resultReturned = command.getTextareaField().split("!");
            for (String individualResult : resultReturned) {

                String[] resultData = individualResult.split(",");

                if (resultData.length > 16 && individualResult.contains("[")) {
                    resultData = util.performRegex(individualResult);
                }

                if (resultData.length != 16) {
                    redirectAttributes.addFlashAttribute("failure", "Please provide date in the below format (Job Id, App_Name, Benchmark, Nodes, Cores, node_name, Result, CPU, OS, BIOS, Cluster, User, Platform, cpu_generation, Run_type, Workload)");
                    return "redirect:/result";
                }
                try {
                    resultService.insertResult(resultData);
                }
                catch (Exception e){
                    redirectAttributes.addFlashAttribute("failMessage", ExceptionUtils.getRootCauseMessage(e));
                    return "redirect:/result";
                }
            }
        }
        else if(command.getRadioButtonSelectedValue().equals("upload")){
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("fileNotUploaded", "Please upload a file");
                return "redirect:/result";
            }

            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                CsvToBean<Result> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Result.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                try {
                    List<Result> results = csvToBean.parse();
                    resultService.insertResultCsv(results);
                }

                catch (Exception exception){
                    redirectAttributes.addFlashAttribute("exceptionMessage", ExceptionUtils.getRootCauseMessage(exception));
                    return "redirect:/result";
                }
            }
            catch (Exception ex) {
                System.out.println(ExceptionUtils.getStackTrace(ex));
            }

        }
        redirectAttributes.addFlashAttribute("successMessage", "Result is successfully inserted!");
        return "redirect:/result";
    }


    @PostMapping("/deleteJob")
    public String deleteJob(
            @ModelAttribute("command") @Valid FormCommand command,
            Model model, Errors errors, RedirectAttributes redirectAttributes) {


                String[] jobIds = command.getTextareaField().split(",");

                try {
                    int count = resultService.deleteJobs(jobIds);


                    if(count == 1 && count == jobIds.length){
                        redirectAttributes.addFlashAttribute("oneJobNotFound", "Job not found" );
                        return "redirect:/deleteJob";
                    }

                    if(count > 1 && count == jobIds.length){
                        redirectAttributes.addFlashAttribute("notFound", count+ " jobs not found" );
                        return "redirect:/deleteJob";
                    }

                    if(count == 1 && jobIds.length > 1){
                        redirectAttributes.addFlashAttribute("oneNotFound", count+" job not found! Successfully deleted remaining" );
                        return "redirect:/deleteJob";
                    }

                    if(count > 1){
                        redirectAttributes.addFlashAttribute("partialSuccess", count+" jobs not found! Successfully deleted remaining" );
                        return "redirect:/deleteJob";
                    }
                    else {
                        redirectAttributes.addFlashAttribute("successMessage", "Successfully deleted!");
                        return "redirect:/deleteJob";
                    }
                }
                catch (Exception e){
                    redirectAttributes.addFlashAttribute("failMessage", ExceptionUtils.getRootCauseMessage(e));
                    return "redirect:/deleteJob";
                }

    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {

        List<String> cpu_list = resultService.getCpu();
        List<String> app_list = resultService.getApp();
        List<String> bm_list = null;
        List<Integer> node_list = resultService.getNodes();
        List<String> os_list = resultService.getOS();
        List<String> bios_list = resultService.getBIOS();
        List<String> cluster_list = resultService.getCluster();
        List<String> user_list = resultService.getUser();
        List<String> platform_list = resultService.getPlatform();
        List<String> cpu_gen_list = resultService.getCpuGen();
        List<String> run_type_list = resultService.getRunType();

        model.addAttribute("cpus", cpu_list);
        model.addAttribute("apps", app_list);
        model.addAttribute("nodes", node_list);
        model.addAttribute("bms", bm_list);
        model.addAttribute("os", os_list);
        model.addAttribute("bios", bios_list);
        model.addAttribute("clusters", cluster_list);
        model.addAttribute("users", user_list);
        model.addAttribute("platforms", platform_list);
        model.addAttribute("cpugens", cpu_gen_list);
        model.addAttribute("runtypes", run_type_list);

        return "resultDashboard";
    }


    @GetMapping("/averagedResults")
    public String showAverageDashboard(Model model) {

        List<String> cpu_gen_list = resultService.getCpuGen();
        List<String> cpu_list = resultService.getCpu();
        List<String> app_list = resultService.getApp();
        List<String> bm_list = null;
        List<Integer> node_list = resultService.getNodes();
        List<String> run_type_list = resultService.getRunType();
        List<Integer> run_count_list = averageResultService.getRunCount();

        model.addAttribute("cpus", cpu_list);
        model.addAttribute("cpugens", cpu_gen_list);
        model.addAttribute("apps", app_list);
        model.addAttribute("nodes", node_list);
        model.addAttribute("bms", bm_list);
        model.addAttribute("runtypes", run_type_list);
        model.addAttribute("runCounts", run_count_list);


        return "resultAverage";
    }


    @GetMapping("/dashboardO")
    public String showDashboardN(Model model) {

        List<String> cpu_list = resultService.getCpu();
        List<String> app_list = resultService.getApp();
        List<String> bm_list = null;
        List<Integer> node_list = resultService.getNodes();
        List<String> os_list = resultService.getOS();
        List<String> bios_list = resultService.getBIOS();
        List<String> cluster_list = resultService.getCluster();
        List<String> user_list = resultService.getUser();
        List<String> platform_list = resultService.getPlatform();
        List<String> cpu_gen_list = resultService.getCpuGen();
        List<String> run_type_list = resultService.getRunType();

        model.addAttribute("cpus", cpu_list);
        model.addAttribute("apps", app_list);
        model.addAttribute("nodes", node_list);
        model.addAttribute("bms", bm_list);
        model.addAttribute("os", os_list);
        model.addAttribute("bios", bios_list);
        model.addAttribute("clusters", cluster_list);
        model.addAttribute("users", user_list);
        model.addAttribute("platforms", platform_list);
        model.addAttribute("cpugens", cpu_gen_list);
        model.addAttribute("runtypes", run_type_list);

        return "resultDashboardO";
    }

    @RequestMapping(value = "/cpusGen", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllCPUs(
            @RequestParam(value = "cpuGen", required = true) String cpuGen) {

        return resultService.getCpu(cpuGen);
    }

    @GetMapping(value = "/resultsExcel")
    public ResponseEntity<InputStreamResource> excelResults() throws IOException {
        List<Result> results = resultService.getAllResults();
        ByteArrayInputStream in = GenerateExcelReport.resultsToExcel(results);
        HttpHeaders headers = new HttpHeaders();
        String str = "result_"+ LocalDate.now().toString()+".xlsx";
        headers.add("Content-Disposition", "attachment; filename="+str);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }

    
    @GetMapping(value = "/uProfPerfAnalyzer")
    public ResponseEntity<Resource> uProfPerfAnalyzer() throws IOException {
        File file = ResourceUtils.getFile("classpath:uProf/EPYC_Perf_Analyzer-uProf_v10.xlsm");

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=EPYC_Perf_Analyzer-uProf_v10.xlsm");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel.sheet.macroEnabled.12"))
                .body(resource);
    }



    @RequestMapping(value = "/resultsCsv", method = RequestMethod.GET)
    public void csvResults(HttpServletResponse response) throws IOException {
        List<Result> results = resultService.getAllResults();
        GenerateCSVReport.writeResults(response.getWriter(), results);
        String str = "result_"+ LocalDate.now().toString()+".csv";
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=results.csv");
    }


    @RequestMapping("/allresultsJson")
    public ResponseEntity<InputStreamResource> getusersJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        List<Result> results = null;
        String exception = null;
        //String arrayToJson = null;
        byte[] buffer = null;
        try {
            results = resultService.getAllResults();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            //arrayToJson = objectMapper.writeValueAsString(results);
            buffer = objectMapper.writeValueAsBytes(results);
        } catch (Exception ex) {
            ex.printStackTrace();
            exception = ex.getMessage();
        }
        //return arrayToJson;
        String str = "result_"+ LocalDate.now().toString()+".json";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename="+str);
        return ResponseEntity
                .ok()
                .contentLength(buffer.length)
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new ByteArrayInputStream(buffer)));
    }

}
