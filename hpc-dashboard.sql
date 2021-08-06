create database results_dashboard;

use results_dashboard;

show tables;


select * from results;

select * from average_result;

select * from results where job_id ="1266a";

#update heat_map set workload="hpc";

select * from heat_map where run_type="latest_aocl";

-- drop table average_result;

-- drop table results;

-- drop table heat_map;



select *  from uprof_raw;

select *  from uprof_reference;

select * from uprof_calculated;



-- drop table uprof_raw;
-- drop table uprof_calculated;

desc uprof_raw;

select  core_0_utilization, core_0_eff_freq, core_0_ipc,  core_0_retired_sse_avx_flops,ccx_0_l3_miss_percent,package_0_total_mem_bw, package_0_total_mem_rd_bw, package_0_total_mem_wr_bw, package_0_approximate_xgmi_outbound_data_bytes from uprof_raw where proc_app_bm="MilanX_fluent_aw14" and run_type="latest";

SET SQL_SAFE_UPDATES = 0;
#ALTER TABLE uprof_raw AUTO_INCREMENT = 1;

select DISTINCT app_name from average_result;

select * from password_reset_token;

#drop table password_reset_token;

select DISTINCT LOWER(cpu_sku) from average_result where app_name="namd" ORDER BY cpu_sku ASC;

select DISTINCT bm_name from results where app_name="openfoam" ORDER BY nodes ASC;

select * from average_result where app_name= "cfx" and cpu_sku IN ("7F72","7F52") and run_type IN ("baseline") and nodes =1 order by case cpu_sku when "7F52" then 1 when "7F72" then 2 end;


select * from app_map;

select * from app_category;

select * from processor_info;

#UPDATE cpu_info SET cpu_sku = LOWER(cpu_sku) where id in (26,27,28,29);

select cpu_sku,cores from average_result group by cpu_sku;

select DISTINCT cpu_sku from average_result where app_name="abaqus" and nodes=1  ;

show tables;


#drop table app_category;

select * from results;


select * from average_result where app_name= "lsdyna" and cpu_sku IN ("7742","7763") and run_type IN ("baseline","sles12sp5_300a_edc") and nodes =1 ORDER BY avg_result DESC;


select * from average_result where cpu_sku="6242" and bm_name="ls-neon";
select * from app_category;
select * from results where cpu="7763" and app_name="hpl";

select * from run_types;

select * from average_result;		
select * from run_types;
select * from average_result where app_name= "cfx" and cpu_sku IN ("7F52","7F72","8268") and run_type IN ("baseline") and nodes =1 ORDER BY avg_result;

select * from average_result where cpu_sku="7763" and run_type="sles12sp5_300a_edc" and nodes=1 and category is not NULL;

select * from average_result where app_name= "openfoam" and cpu_sku IN ("7543","74F3") and run_type IN ("latest") and nodes =1 ORDER BY case cpu_sku when "7543" then 1 when "74F3" then 2 else 9999 end;

set innodb_lock_wait_timeout=200;

show variables like 'innodb_lock_wait_timeout';

select * from average_result where  app_name="openfoam" and cpu_sku="7763" and run_type="latest";

select * from heat_map where cpu_sku="7763"  and nodes=1 and category is not NULL;

#delete from average_result where run_type="v195";

#update heat_map set category="FEA Explicit"  where bm_name="ls-odb-10m";
#update heat_map set isv="LSTC"  where bm_name="ls-odb-10m";


select DISTINCT isv from heat_map where category="Computational Fluid Dynamics" and cpu_sku="Rome64" and run_type="freq_2933";	

select DISTINCT app_name from app_category where isv="ansys";

select * from heat_map;

select DISTINCT ISV from heat_map where cpu_sku="Rome64" and run_type="freq_2933" and nodes=1 and category="Computational Fluid Dynamics";

select DISTINCT app_name from heat_map where cpu_sku="Milan64" and run_type="freq_2933" and isv="Ansys";

select bm_name from heat_map where cpu_sku="Milan64" and run_type="freq_2933" and nodes=1 and category is not NULL;

select bm_name from heat_map where cpu_sku="Rome64" and run_type="freq_2933" and nodes=1 and category is not NULL;

select * from heat_map where cpu_sku in ("Milan64","Rome64") and run_type in ("baseline","freq_2933") and category is not NULL;

select precision_info from applications where app_name="openfoam";

select * from average_result where app_name="openfoam" and cpu_sku in ("7F52","7F72") and nodes=1;

select DISTINCT cpu from results where cpu_gen="rome" ORDER BY cpu ASC;

SET SQL_SAFE_UPDATES = 0;

select * from benchmarks;

select *  from results where run_type like "%scaling%";

select *  from results;

select DISTINCT bios_ver from results ORDER BY os ASC;

select * from heat_map where cpu_sku="7702" and app_name="openfoam";

SELECT * FROM results where bm_name="aba-e13" and cpu="Milan64_3200" and nodes=1 and run_type="baseline";

SELECT  * FROM  results WHERE app_name='cfx'and cpu="Milan64_3200" and run_type="freq_2933";

SELECT  DISTINCT (bm_name) FROM  results WHERE app_name='abaqus'and cpu="Rome64_3200" and run_type="freq_2933";

SELECT  DISTINCT LOWER(run_type) FROM  average_result WHERE app_name='cfx' and cpu_sku='6246' ORDER BY run_type ASC;

select * from average_result where app_name= 'cfx' and cpu_sku like "Milan64%" and nodes =1 and run_type="freq_2933" ORDER BY bm_name;

select * from average_result where cpu_sku in ( "Milan64_3200", "Milan64_2933", "Rome64_2933") and run_type in("baseline","freq_2933");

select * from average_result where app_name= 'abaqus' and cpu_sku IN ("Milan64_3200", "Milan64_2933", "Rome64_2933") and run_type in("baseline","freq_2933") and nodes =1 ORDER BY bm_name,avg_result DESC;

select * from app_category;

select DISTINCT cpu_sku from average_result where run_type in("baseline","freq_2933");

select * from average_result where app_name="openfoam"  and cpu_sku in( "7F52", "7F32", "7F72") and nodes =1 ORDER BY avg_result DESC;


select * from average_result where app_name="openfoam" and cpu_sku IN ("7F52", "7F32", "7F72") and nodes =1 ORDER BY bm_name,avg_result DESC;

select * from average_result where app_name="namd" and cpu_sku IN ("7F52", "7F32", "7F72") and nodes =1 ORDER BY bm_name,avg_result;

select * from average_result where app_name="namd" and cpu_sku IN ("n1-standard-96", "n2d-standard-224", "n2d-highcpu-224","n1-highcpu-96") and nodes =1 ORDER BY bm_name;

#update applications set precision_info="Elapsed Time" where app_id="openfoam_v1906";

select DISTINCT app_name from average_result;


select DISTINCT app_name from results ORDER BY app_name ASC;

select DISTINCT bm_name from results ORDER BY bm_name ASC;

select DISTINCT nodes from results ORDER BY nodes ASC;

select DISTINCT cpu from results ORDER BY cpu ASC;

select * from run_types;

select * from heat_map where cpu_sku in ("6242","6244") and run_type in ("legacy","legacy") and nodes=1  and category is not NULL;

select * from app_category;

select * from user;
select * from role;
select * from users_roles;



update role set name="ROLE_TEAM" where id=11;


#insert into users_roles (user_id,role_id) values (17,7),(18,7),(19,7);


#update users_roles set role_id=2 where user_id=5 and role_id=6;

#update applications set comp_flags="-march=znver2 -O3" where app_id="openfoam_v1906";


select app_name from benchmarks;

delete from users_roles where user_id=126;
delete from user where id=126;


SELECT * from average_result where cpu_sku ="7F52" and app_name ="openfoam" ORDER BY bm_name;

#delete from my_user_details where id=1;
#ALTER TABLE benchmarks auto_increment=1;


drop table average_result;

drop table results;

drop table heat_map;


SELECT DISTINCT a.cpu_sku FROM  average_result AS a WHERE  a.app_name = "wrf" and a.nodes=1 and a.run_type IN ("freq_2933") ORDER BY cpu_sku ASC;
SELECT DISTINCT a.cpu_sku FROM  average_result AS a WHERE  a.app_name = "wrf" and a.nodes=1 and a.run_type IN ("freq_2933") ORDER BY cpu_sku ASC;

SELECT DISTINCT a.cpu_sku FROM  average_result AS a WHERE  a.app_name = "wrf" and a.nodes=1  AND a.cpu_sku NOT IN (SELECT b.cpu_sku  FROM average_result AS b WHERE  b.cpu_sku = '7742') ORDER BY cpu_sku ASC;

#drop table benchmarks;

#drop table applications;

#drop table cpu_info;

ALTER TABLE results
ADD COLUMN inserted_at 
  TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
  ON UPDATE CURRENT_TIMESTAMP;


-- Indexes

show indexes from results_dashboard.results;

CREATE INDEX cpu_index ON results_dashboard.results (cpu);
