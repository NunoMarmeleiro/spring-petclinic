package org.springframework.samples.petclinic.pet;

import org.springframework.samples.petclinic.pet.domain.Pet;

import java.util.List;

public interface PetManagement {
	void deletePets(int ownerId);
}
