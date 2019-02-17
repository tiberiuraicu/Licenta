package com.server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.server.entites.Notification;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface NotificareRepository extends JpaRepository<Notification, Integer> {
}