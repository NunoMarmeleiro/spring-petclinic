package org.springframework.samples.petclinic.pet.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.samples.petclinic.visit.VisitService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

	private final PetRepository pets;
	private final VisitService visitService;

	@Override
	public Pet findPet(int petId) {
		return this.pets.findById(petId);
	}

	@Override
	public List<Pet> findByOwnerId(int ownerId) {
		return this.pets.findByOwnerId(ownerId);
	}

	@Override
	public void deletePets(int ownerId) {
		List<Pet> petIds = this.pets.findByOwnerId(ownerId);
		for(Pet i : petIds) {
			this.visitService.deleteVisits(i.getId());
		}
		this.pets.deletePetByOwnerId(ownerId);
	}
}
