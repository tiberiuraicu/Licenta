package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.entites.User;
;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends JpaRepository<User, Integer> {
	
     User getUserByEmail(String email);

}