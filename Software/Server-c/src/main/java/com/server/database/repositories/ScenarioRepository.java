package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.server.entites.Scenario;

public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {
	Scenario getScenarioBySensorName(String sensorName);
} 


