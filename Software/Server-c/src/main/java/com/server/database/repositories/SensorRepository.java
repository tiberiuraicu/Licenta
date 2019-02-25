package com.server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.entites.Sensor;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface SensorRepository extends JpaRepository<Sensor, Integer> {

//	List<Sensor> getSensorByName(String name);
	Sensor getSensorByName(String name);

}