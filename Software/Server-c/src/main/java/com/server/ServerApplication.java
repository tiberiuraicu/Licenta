package com.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.server.rest.security.JwtFilter;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class ServerApplication {
	
	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/user/resources/*");
		registrationBean.addUrlPatterns("/rest1/*");
		return registrationBean;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
	
}

