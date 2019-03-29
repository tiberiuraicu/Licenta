package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.server.entites.Circuit;
import com.server.entites.PowerSource;
public interface CircuitRepository extends JpaRepository<Circuit, Integer> {
	Circuit getCircuitById(int id);
}
