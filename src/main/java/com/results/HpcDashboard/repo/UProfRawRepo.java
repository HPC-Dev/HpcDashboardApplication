package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.AppCategory;
import com.results.HpcDashboard.models.UProfRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UProfRawRepo extends JpaRepository<UProfRaw,Integer> {

    public static final String FIND_UProf_RAW = "select * from uprof_raw";

    @Query(value = FIND_UProf_RAW, nativeQuery = true)
    public List<UProfRaw> findAllUProf_RAW();
}