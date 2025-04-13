package org.springframework.samples.petclinic.visit.infrastructure;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.visit.domain.Visit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VisitRepository extends Repository<Visit, Integer> {

	@Query("SELECT visit FROM Visit visit WHERE visit.petId =:id")
	@Transactional(readOnly = true)
	List<Visit> findByPetId(@Param("id") Integer id);

	void save(Visit visit);

	@Modifying
	@Transactional
	@Query("DELETE FROM Visit visit WHERE visit.id = :id")
	void deleteVisitById(@Param("id") Integer id);

	@Modifying
	@Transactional
	@Query("DELETE FROM Visit visit WHERE visit.petId = :id")
	void deleteVisitByPetId(@Param("id") Integer id);

}
