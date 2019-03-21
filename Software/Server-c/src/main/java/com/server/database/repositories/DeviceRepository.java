package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.entites.Device;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface DeviceRepository extends JpaRepository<Device, Integer> {

	Device getDispozitivByName(String name);
	
}