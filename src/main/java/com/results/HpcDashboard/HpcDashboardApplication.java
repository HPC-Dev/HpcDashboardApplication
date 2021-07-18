package com.results.HpcDashboard;


import com.results.HpcDashboard.repo.ApplicationRepo;
import com.results.HpcDashboard.repo.BenchmarkRepo;
import com.results.HpcDashboard.repo.CPURepo;
import com.results.HpcDashboard.services.AverageResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HpcDashboardApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(HpcDashboardApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

	@Bean
	public Docket apiMonitoramento() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.results"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("HPC Dashboard Controller")
				.description("")
				.build();
	}

	@PostConstruct
	public void dateTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("CST"));
	}

}
