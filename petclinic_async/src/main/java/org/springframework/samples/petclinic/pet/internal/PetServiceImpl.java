package org.springframework.samples.petclinic.pet.internal;

import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetService;

import java.util.List;

public class PetServiceImpl implements PetService {

	private final PetRepository pets;

	public PetServiceImpl(PetRepository petRepo) {
		this.pets = petRepo;
	}

	@Override
	public Pet findPet(int petId) {
		return this.pets.findById(petId);
	}

	@Override
	public List<Pet> findByOwnerId(int ownerId) {
		return this.pets.findByOwnerId(ownerId);
	}
}
