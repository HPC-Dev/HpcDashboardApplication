package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.*;
import com.results.HpcDashboard.repo.AppCategoryRepo;
import com.results.HpcDashboard.repo.AppMapRepo;
import com.results.HpcDashboard.repo.ProcessorRepo;
import com.results.HpcDashboard.repo.UserRepository;
import com.results.HpcDashboard.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/helper")
public class HelperRestController {


    @Autowired
    Util util;

    @Autowired
    AppMapRepo appMapRepo;

    @Autowired
    ProcessorRepo processorRepo;

    @Autowired
    AppCategoryRepo appCategoryRepo;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/appMetricStatus")
    public List<AppMap> getAppMap(){

        List<AppMap> list = null;
        list = appMapRepo.findAllAppMap();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }


    @GetMapping("/appCategory")
    public List<AppCategory> getAppCategory(){

        List<AppCategory> list = null;
        list = appCategoryRepo.findAllAppCategory();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }

    @GetMapping("/procStatus")
    public List<Processor> getProc(){

        List<Processor> list = null;
        list = processorRepo.findAllProcessors();
        if(list ==null){
            return Collections.emptyList();
        }
        return list;
    }

    @GetMapping("/userList")
    public List<Map<String,String>> getUsers(){

        List<User> list = null;
        list = userRepository.findAll();
        if(list ==null){
            return Collections.emptyList();
        }

        List<Map<String,String>> userMap = new ArrayList<>();

        Map<String,String> map;

        for(User user: list)
        {
            map = new HashMap<>();
            map.put("First Name",user.getFirstName());
            map.put("Last Name",user.getLastName());
            map.put("Email",user.getEmail());
            Collection<Role> roles = user.getRoles();
            String role = "";
            for (Role r : roles) {
                if(role == "")
                    role += r.getName().substring(5);
                else
                    role += ", "+ r.getName().substring(5);
            }
            map.put("Roles",role);
            map.put("Verified", String.valueOf(user.isEnabled()));
            map.put("Approved", String.valueOf(user.isApproved()));

            userMap.add(map);

        }
        return userMap;
    }


}
