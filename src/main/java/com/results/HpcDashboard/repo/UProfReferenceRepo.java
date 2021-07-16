package com.results.HpcDashboard.repo;

import com.results.HpcDashboard.models.UProfCalculated;
import com.results.HpcDashboard.models.UProfRaw;
import com.results.HpcDashboard.models.UProfReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UProfReferenceRepo extends JpaRepository<UProfReference,String> {

    public static final String FIND_UProf_Reference = "select * from uprof_reference WHERE processor=:cpu";

    @Query(value = FIND_UProf_Reference, nativeQuery = true)
    public UProfReference findUProf_Reference(String cpu);

}