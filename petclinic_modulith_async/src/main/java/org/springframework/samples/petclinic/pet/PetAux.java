package org.springframework.samples.petclinic.pet;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.pet.domain.Pet;
import org.springframework.samples.petclinic.pet.infrastructure.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetAux {

	private final PetRepository pets;

	public List<Pet> findByOwnerId(int ownerId) {
		return this.pets.findByOwnerId(ownerId);
	}

}
