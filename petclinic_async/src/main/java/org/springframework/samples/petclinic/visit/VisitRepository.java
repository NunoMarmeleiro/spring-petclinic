package org.springframework.samples.petclinic.visit;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VisitRepository extends Repository<Visit, Integer> {

	@Query("SELECT visit FROM Visit visit WHERE visit.petId =:id")
	@Transactional(readOnly = true)
	List<Visit> findByPetId(@Param("id") Integer id);

	void save(Visit visit);
}
