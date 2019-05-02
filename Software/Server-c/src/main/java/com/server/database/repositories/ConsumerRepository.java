package com.server.database.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.server.entites.Consumer;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ConsumerRepository extends JpaRepository<Consumer, Integer> {

	List<Consumer> getConsumerByName(String name);

	List<Consumer> findTop60ByNameOrderByIdDesc(String name);

	List<Consumer> findTop3600ByNameOrderByIdDesc(String name);

	Consumer findTopByNameOrderByIdDesc(String name);

	List<Consumer> findTop1800ByNameOrderByIdDesc(String name);

	@Query(value = "SELECT name FROM Consumer WHERE circuit != null")
	List<String> findAllNotNull();

	@Query(value = "SELECT powerConsumed FROM Consumer WHERE name= :name and timestamp like CONCAT('%',:time,'%')")
	List<Double> findConsumerRecordAtSpecificHour(@Param("name") String name, @Param("time") String time);

	List<Consumer> findAllByName(String name);

	@Query(value = "SELECT sum(powerConsumed)/count(powerConsumed) FROM Consumer WHERE name= :name and timestamp like CONCAT('%',:time,'%')")
	Double findSumConsumerRecordAtSpecificHour(@Param("name") String name, @Param("time") String time);

	@Query(value = "SELECT sum(power_consumed)/count(power_consumed) FROM consumer "
			+ "WHERE name= :name and timestamp like CONCAT('%',:time,'%')", nativeQuery = true)
	Double findSumConsumerRecordAtSpecificDay(@Param("name") String name, @Param("time") String time);
}