package com.server.Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.server.Server.cep.processing.FnctiiAjutor;
import com.server.Server.database.repositories.AlimentatorRepository;
import com.server.Server.database.repositories.ConsumatorRepository;
import com.server.Server.database.repositories.DispozitivRepository;
import com.server.Server.database.repositories.NotificareRepository;
import com.server.Server.database.repositories.UserRepository;


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

