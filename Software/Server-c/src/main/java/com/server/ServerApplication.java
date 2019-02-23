package com.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.server.cep.processing.FnctiiAjutor;
import com.server.database.repositories.PowerSourceRepository;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class ServerApplication implements CommandLineRunner {

	
	@Autowired
	private PowerSourceRepository alimentatorRepository;
	
	@Autowired
	private FnctiiAjutor fnctiiAjutor;
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
	
	@Override
	public void run(String... strings) throws Exception {	
	
		alimentatorRepository.save(fnctiiAjutor.getAlimentator());
		
	}
}

