package com.server.database.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.server.entites.Consumer;
import com.server.entites.Sensor;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface SensorRepository extends JpaRepository<Sensor, Integer> {

	List<Sensor> getSensorByName(String name);

	Sensor findTopByNameOrderByIdDesc(String name);

	List<Sensor> findTop3600ByNameOrderByIdDesc(String name);

	List<Sensor> findTop60ByNameOrderByIdDesc(String name);

	@Query(value = "SELECT name FROM Sensor WHERE circuit != null")
	List<String> findAllNotNull();
	
	
	List<Sensor> findAllByName(String name);

}