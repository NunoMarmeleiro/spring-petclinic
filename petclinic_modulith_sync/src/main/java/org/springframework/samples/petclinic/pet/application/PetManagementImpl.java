package org.springframework.samples.petclinic.pet.application;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.pet.domain.Pet;
import org.springframework.samples.petclinic.pet.PetManagement;
import org.springframework.samples.petclinic.pet.infrastructure.PetRepository;
import org.springframework.samples.petclinic.visit.VisitManagement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetManagementImpl implements PetManagement {

	private final PetRepository pets;
	private final VisitManagement visitManagement;

	@Override
	public void deletePets(int ownerId) {
		List<Pet> petIds = this.pets.findByOwnerId(ownerId);
		for(Pet i : petIds) {
			this.visitManagement.deleteVisits(i.getId());
		}
		this.pets.deletePetByOwnerId(ownerId);
	}
}
