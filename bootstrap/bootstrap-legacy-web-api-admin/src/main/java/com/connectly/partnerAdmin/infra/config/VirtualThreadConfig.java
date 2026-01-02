package com.connectly.partnerAdmin.infra.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration
public class VirtualThreadConfig implements AsyncConfigurer {


	@Bean(name = "virtualThreadExecutor")
	public ExecutorService virtualThreadExecutor() {
		return Executors.newVirtualThreadPerTaskExecutor();
	}

}
