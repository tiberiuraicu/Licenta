package com.server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.server.entites.Consumer;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ConsumerRepository extends JpaRepository<Consumer, Integer> {

	List<Consumer> getConsumerByName(String name);
	//Consumer getConsumerByName(String name);


}