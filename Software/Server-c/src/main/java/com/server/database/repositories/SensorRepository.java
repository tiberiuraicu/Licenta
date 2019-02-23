package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.entites.Sensor;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface SensorRepository extends JpaRepository<Sensor, Integer> {

	Sensor getSensorByName(String name);

}