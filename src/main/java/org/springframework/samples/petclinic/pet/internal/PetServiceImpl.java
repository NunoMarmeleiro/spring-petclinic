package org.springframework.samples.petclinic.pet.internal;

import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetService;

public class PetServiceImpl implements PetService {
	private final PetRepository pets;
	public PetServiceImpl(PetRepository petRepo) {
		this.pets = petRepo;
	}
	@Override
	public Pet findPet(int petId) {
		return this.pets.findById(petId);
	}
}
