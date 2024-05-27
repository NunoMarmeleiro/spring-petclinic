package org.springframework.samples.petclinic.owner.infrastructure;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.samples.petclinic.owner.application.OwnerManagement;
import org.springframework.samples.petclinic.pet.DeletedOwner;
import org.springframework.samples.petclinic.pet.PetAux;

@ApplicationModuleTest
@RequiredArgsConstructor
public class OwnerIntegrationTests {

	private final OwnerManagement ownerManagement;

	@MockBean
	PetAux petAux;

	@Test
	void publishesOwnerDeletion(Scenario scenario) {

		scenario.stimulate(() -> ownerManagement.deleteOwner(1))
			.andWaitForEventOfType(DeletedOwner.class)
			.matchingMappedValue(DeletedOwner::ownerId, 1)
			.toArrive();
	}
}
