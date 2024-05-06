package org.springframework.samples.petclinic.pet;

import org.springframework.samples.petclinic.pet.domain.Pet;

import java.util.List;

public interface PetService {

	List<Pet> findByOwnerId(int ownerId);
	void deletePets(int ownerId);
}
