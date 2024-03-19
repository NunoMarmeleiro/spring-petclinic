package org.springframework.samples.petclinic.visit;

import org.springframework.data.repository.Repository;

public interface VisitRepository extends Repository<Visit, Integer> {

	void save(Visit visit);
}
