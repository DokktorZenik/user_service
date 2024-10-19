package com.task_manager.user_service;

import com.task_manager.user_service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceApplication {
	@Autowired
	RoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public ApplicationRunner initializeRoles(){
		return args -> {
			roleService.initializeRoles();
		};
	}

}
