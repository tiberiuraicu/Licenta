package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.server.entites.PowerSource;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PowerSourceRepository extends JpaRepository<PowerSource, Integer> {}