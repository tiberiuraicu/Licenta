package com.server.database.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.entites.Consumer;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ConsumerRepository extends JpaRepository<Consumer, Integer> {

	List<Consumer> getConsumerByName(String name);

	List<Consumer> findTop60ByNameOrderByIdDesc(String name);

	List<Consumer> findTop3600ByNameOrderByIdDesc(String name);

	Consumer findTopByNameOrderByIdDesc(String name);

	List<Consumer> findTop1800ByNameOrderByIdDesc(String name);

	@Query(value = "SELECT name FROM Consumer WHERE circuit != null")
	List<String> findAllNotNull();
	
	
	List<Consumer> findAllByName(String name);

}