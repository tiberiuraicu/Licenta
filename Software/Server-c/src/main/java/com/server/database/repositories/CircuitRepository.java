package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.server.entites.Circuit;
public interface CircuitRepository extends JpaRepository<Circuit, Integer> {
	Circuit getCircuitById(int id);
}
