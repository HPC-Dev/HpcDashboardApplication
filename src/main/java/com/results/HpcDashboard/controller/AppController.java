package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.FormCommand;
import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.repo.UProfCalculatedRepo;
import com.results.HpcDashboard.services.*;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.*;


@Controller
public class AppController {

    @Autowired
    UserService userService;

    @Autowired
    AverageResultService averageResultService;

    @Autowired
    UProfCalculatedRepo uProfCalculatedRepo;

    @Autowired
    Util util;

    @ModelAttribute("command")
    public FormCommand formCommand() {
        return new FormCommand();
    }

    @RequestMapping({ "/", "/index" })
    public String index(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if(user!=null){
            model.addAttribute("user", user.getFirstName());
        }
        return "index";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
     return "login";
    }

    @GetMapping("/forgotPassword")
    public String showForgetPasswordPage1(){
        return "forgotPassword";
    }


    @GetMapping("/mChartOld")
    public String showMultiCharts(Model model) {
        List<String> app_list = averageResultService.getApp();
        List<String> cpu_list = averageResultService.getCpu();
        model.addAttribute("cpus", cpu_list );
        model.addAttribute("apps", app_list );
        return "multiChartsOld";
    }

    @GetMapping("/multiCPUCharts")
    public String showMultiChartsNew(Model model) {
        List<String> app_list = averageResultService.getApp();
        List<String> cpu_list = averageResultService.getCpu();
        model.addAttribute("cpus", cpu_list );
        model.addAttribute("apps", app_list );
        return "multiCPUCharts";
    }

    @GetMapping("/singleCPUCharts")
    public String showCharts(Model model) {
        List<String> cpu_list = averageResultService.getCpu();
        Map<String, List<String>> cpuMap = util.getCPUDropdown(cpu_list);

        model.addAttribute("cpuMap", cpuMap );
        model.addAttribute("cpus", cpu_list );
        return "singleCPUCharts";
    }

    @RequestMapping(value = "/bms", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllBMs(
            @RequestParam(value = "appName", required = true) String appName, @RequestParam(value = "cpu", required = true) String cpu) {

        return averageResultService.getSelectBm(appName,cpu);
    }


    @RequestMapping(value = "/bmsDashboard", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllBMs(
            @RequestParam(value = "appName", required = true) String appName) {

        return averageResultService.getSelectBm(appName);
    }

    @GetMapping("/singleCPUResult")
    public String showData(Model model) {

        List<String> app_list = averageResultService.getApp();
        List<String> cpu_list = averageResultService.getCpu();

        Map<String, List<String>> cpuMap = util.getCPUDropdown(cpu_list);

        model.addAttribute("cpuMap", cpuMap );
        model.addAttribute("cpus", cpu_list );
        model.addAttribute("apps", app_list );
        return "singleCPUResult";
    }

    @GetMapping("/scalingComparison")
    public String showDataCompare(Model model) {

        List<String> app_list = averageResultService.getApp();
        List<String> cpu_list = averageResultService.getCpu();
        model.addAttribute("cpus", cpu_list );
        model.addAttribute("apps", app_list );
        return "scalingComparison";
    }

    @GetMapping("/partComparison")
    public String showDataComparison(Model model) {

        List<String> app_list = averageResultService.getApp();
        model.addAttribute("apps", app_list );
        return "partComparison";
    }

    @GetMapping("/heatMap")
    public String showHeatMap(Model model) {

        List<String> cpu_list = averageResultService.getJustCpu();
        Map<String, List<String>> cpuMap = util.getCPUDropdown(cpu_list);

        model.addAttribute("cpuMap", cpuMap );

        model.addAttribute("cpus", cpu_list);
        return "heatMap";
    }


    @GetMapping("/uProfRadar")
    public String showuProfRadar(Model model) {

        List<String> cpu_list = uProfCalculatedRepo.findAllCPUs();
        uProfCalculatedRepo.findAllCPUs();

        model.addAttribute("cpus", cpu_list);
        return "uProfRadar";
    }

    @RequestMapping(value = "/cpus", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, List<String>> findAllCPUs(
            @RequestParam(value = "appName", required = true) String appName) {

        List<String> cpu_list = averageResultService.getCpu(appName);

        return util.getCPUDropdown(cpu_list);
    }

    @RequestMapping(value = "/runTypes", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllrunTypes(
            @RequestParam(value = "appName", required = true) String appName) {

        return averageResultService.getRunTypes(appName);
    }

    @RequestMapping(value = "/runTypesByCPU", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllrunTypesByCPU(
            @RequestParam(value = "cpu", required = true) String cpu) {

        return averageResultService.getRunTypesByCPU(cpu);
    }


    @RequestMapping(value = "/runTypesByCPUUProf", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllrunTypesByCPUUProf(
            @RequestParam(value = "cpu", required = true) String cpu) {

        return uProfCalculatedRepo.getRunTypesByCPUUProf(cpu);
    }

    @RequestMapping(value = "/runTypesByAPPCPU", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllrunTypesByAPPCPU(
            @RequestParam(value = "appName", required = true) String appName, @RequestParam(value = "cpu", required = true) String cpu) {

        return averageResultService.getRunTypesByAPPCPU(appName,cpu);
    }

    @GetMapping("/cpusSelected/{app_name}")
        public  @ResponseBody List<String> findAllCpusSelected(@PathVariable("app_name") String app_name,  String[] runTypes) {
        List<String> runType = Arrays.asList(runTypes);
            return averageResultService.getCpuSelected(app_name,runType);
    }

    @RequestMapping(value = "/apps", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAllApps(
            @RequestParam(value = "cpu", required = true) String cpu) {

        return averageResultService.getApp(cpu);
    }

    @RequestMapping(value = "/appsByType", method = RequestMethod.GET)
    public @ResponseBody
    List<String> findAppsByType(
            @RequestParam(value = "cpu", required = true) String cpu, @RequestParam(value = "type", required = true) String type) {

        return averageResultService.getAppByType(cpu, type);
    }
}
