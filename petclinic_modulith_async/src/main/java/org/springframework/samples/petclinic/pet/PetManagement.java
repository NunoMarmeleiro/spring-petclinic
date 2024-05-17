package org.springframework.samples.petclinic.pet;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.samples.petclinic.pet.DeletedOwner;
import org.springframework.samples.petclinic.pet.DeletedPet;
import org.springframework.samples.petclinic.pet.DeletedPets;
import org.springframework.samples.petclinic.pet.domain.Pet;
import org.springframework.samples.petclinic.pet.infrastructure.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetManagement {

	private final ApplicationEventPublisher events;

	private final PetRepository pets;

	@ApplicationModuleListener
	void on(DeletedOwner event) throws InterruptedException {
		List<Pet> pets = this.pets.findByOwnerId(event.ownerId());
		List<Integer> petIds = new ArrayList<>();
		for (Pet i : pets) {
			petIds.add(i.getId());
		}
		deleteVisitsFromPets(petIds);
		this.pets.deletePetByOwnerId(event.ownerId());
	}

	@Transactional
	public void deleteVisitsFromPets(List<Integer> petIds) {
		events.publishEvent(new DeletedPets(petIds));
	}

	@Transactional
	public void deleteVisits(int petId) {
		events.publishEvent(new DeletedPet(petId));
	}

	public List<Pet> findByOwnerId(int ownerId) {
		return this.pets.findByOwnerId(ownerId);
	}

}
