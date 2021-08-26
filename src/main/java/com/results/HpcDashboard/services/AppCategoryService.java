package com.results.HpcDashboard.services;

import com.results.HpcDashboard.models.AppCategory;
import com.results.HpcDashboard.repo.AppCategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppCategoryService {
    @Autowired
    AppCategoryRepo appCategoryRepo;


    public AppCategory getSingleCategory(String bm_name)
    {
        AppCategory appCategory = null;

        if(appCategoryRepo.existsById(bm_name))
            appCategory = appCategoryRepo.getOne(bm_name);
        else
            appCategory = new AppCategory();

        return appCategory;
    }


    public void insertAppCategory(List<AppCategory> categories){
        for(AppCategory category: categories) {
            appCategoryRepo.save(category);
        }
    }
}
