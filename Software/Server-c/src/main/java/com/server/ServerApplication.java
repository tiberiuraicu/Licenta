package com.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.server.cep.processing.FnctiiAjutor;
import com.server.database.repositories.AlimentatorRepository;
import com.server.database.repositories.ConsumatorRepository;
import com.server.database.repositories.DispozitivRepository;
import com.server.database.repositories.NotificareRepository;
import com.server.database.repositories.UserRepository;


@SpringBootApplication

public class ServerApplication implements CommandLineRunner {

	
	@Autowired
	private AlimentatorRepository alimentatorRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
	
	@Override
	public void run(String... strings) throws Exception {	
	
		
		FnctiiAjutor fnctiiAjutor = new FnctiiAjutor();
		alimentatorRepository.save(fnctiiAjutor.getAlimentator());
		
	}
}

