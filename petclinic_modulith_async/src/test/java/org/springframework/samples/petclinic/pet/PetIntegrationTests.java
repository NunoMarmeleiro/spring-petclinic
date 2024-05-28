package org.springframework.samples.petclinic.pet;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.samples.petclinic.pet.infrastructure.PetManagement;

@ApplicationModuleTest
@RequiredArgsConstructor
public class PetIntegrationTests {

	private final PetManagement petManagement;

	@Test
	void publishesPetDeletion(Scenario scenario) {

		scenario.stimulate(() -> petManagement.deleteVisits(1))
			.andWaitForEventOfType(DeletedPet.class)
			.matchingMappedValue(DeletedPet::petId, 1)
			.toArrive();
	}

}
