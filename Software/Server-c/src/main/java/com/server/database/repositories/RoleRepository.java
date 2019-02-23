package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.entites.Role;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface RoleRepository extends JpaRepository<Role, Integer> {}