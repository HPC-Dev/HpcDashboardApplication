package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.models.Result;
import com.results.HpcDashboard.repo.ResultRepo;
import com.results.HpcDashboard.services.AverageResultService;
import com.results.HpcDashboard.services.ResultService;
import com.results.HpcDashboard.util.Util;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/res")
@RestController
public class ResultFormRestController {

    @Autowired
    ResultService resultService;

    @PostMapping(value = "/resultJson", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> insertResult(@RequestBody List<Result> results) {
        if(results != null || results.size() > 0 ) {
            try {
                resultService.insertResultCsv(results);
            } catch (Exception e) {
                return new ResponseEntity(ExceptionUtils.getRootCauseMessage(e) +"\n" , HttpStatus.OK);
            }
        }
        return new ResponseEntity("Success! \n",HttpStatus.OK);
    }

    @GetMapping(value = "/resultJson")
    public List<Result> getResults() {
        List<Result> list = resultService.getAllResults();
        return list;
    }

}
