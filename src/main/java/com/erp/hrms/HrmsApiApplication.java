package com.erp.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;


@SpringBootApplication
@EnableScheduling
//@ComponentScan({"com.erp.hrms.api.controller","com.erp.hrms.api.dao","com.erp.hrms.api.dao",
//"com.erp.hrms.api.entity","com.erp.hrms.api.repo","com.erp.hrms.api.security.config","com.erp.hrms.api.security.utll",	
//"com.erp.hrms.api.service.impl","com.erp.hrms.entity"
//})
public class HrmsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrmsApiApplication.class, args);
	}

}
