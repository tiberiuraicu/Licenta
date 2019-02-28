package com.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.server.cep.processing.FunctiiAjutor;
import com.server.database.repositories.PowerSourceRepository;
import com.server.entites.NormalPowerSource;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
public class DummyApplication implements CommandLineRunner {

	
	@Autowired
	private PowerSourceRepository powerSourceRepository;
	
	@Autowired
	private FunctiiAjutor fnctiiAjutor;
	
	public static void main(String[] args) {
		SpringApplication.run(DummyApplication.class, args);
	}
	
	@Override
	public void run(String... strings) throws Exception {	
		NormalPowerSource normalPowerSource=new NormalPowerSource();
		powerSourceRepository.save(normalPowerSource);
		powerSourceRepository.save(fnctiiAjutor.getAlimentator());
	}
}

