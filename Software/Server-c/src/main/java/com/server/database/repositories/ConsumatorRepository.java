package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.server.entites.Consumer;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ConsumatorRepository extends JpaRepository<Consumer, Integer> {

	Consumer getConsumatorByType(String type);
	Consumer findByName(String name);

}