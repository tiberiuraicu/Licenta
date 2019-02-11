package com.server.Server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.server.Server.entites.Consumator;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@EnableJpaRepositories(basePackages = "com.example.demo.repositories")
public interface ConsumatorRepository extends JpaRepository<Consumator, Integer> {

	Consumator getConsumatorByTip(String tip);
	Consumator findByNume(String nume);

}