package com.server.Server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.Server.entites.Senzor;



// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface SenzorRepository extends JpaRepository<Senzor, Integer> {

	Senzor getSenzorByNume(String nume);

}