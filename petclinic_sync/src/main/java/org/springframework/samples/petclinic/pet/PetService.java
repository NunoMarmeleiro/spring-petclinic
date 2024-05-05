package org.springframework.samples.petclinic.pet;

import java.util.List;

public interface PetService {

	Pet findPet(int petId);
	List<Pet> findByOwnerId(int ownerId);
	void deletePets(int ownerId);
}
