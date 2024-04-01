package org.springframework.samples.petclinic.pet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PetRepository extends Repository<Pet, Integer> {

	@Query("SELECT pet FROM Pet pet WHERE pet.id =:id")
	@Transactional(readOnly = true)
	Pet findById(@Param("id") Integer id);

	@Query("SELECT pet FROM Pet pet WHERE pet.ownerId =:id AND pet.name =:name")
	@Transactional(readOnly = true)
	Pet findByOwnerIdAndName(@Param("id") Integer id, @Param("name") String petName);

	void save(Pet pet);

	/**
	 * Retrieve all {@link PetType}s from the data store.
	 * @return a Collection of {@link PetType}s.
	 */
	@Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
	@Transactional(readOnly = true)
	List<PetType> findPetTypes();
}
