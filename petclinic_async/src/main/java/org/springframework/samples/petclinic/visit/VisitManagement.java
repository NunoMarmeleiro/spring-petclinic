package org.springframework.samples.petclinic.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;

import org.springframework.samples.petclinic.pet.DeletedPet;
import org.springframework.samples.petclinic.pet.DeletedPets;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitManagement {

	private final VisitRepository visits;

	@ApplicationModuleListener
	void on(DeletedPets event) throws InterruptedException {
		for(Integer i : event.petIds()) {
			this.visits.deleteVisitByPetId(i);
		}
	}
	@ApplicationModuleListener
	void on(DeletedPet event) throws InterruptedException {
		this.visits.deleteVisitByPetId(event.petId());
	}
}
