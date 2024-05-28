package org.springframework.samples.petclinic.owner.infrastructure;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.samples.petclinic.owner.DeletedOwner;

@ApplicationModuleTest
@RequiredArgsConstructor
public class OwnerIntegrationTests {

	private final OwnerManagement ownerManagement;

	@Test
	void publishesOwnerDeletion(Scenario scenario) {

		scenario.stimulate(() -> ownerManagement.deleteOwner(1))
			.andWaitForEventOfType(DeletedOwner.class)
			.matchingMappedValue(DeletedOwner::ownerId, 1)
			.toArrive();
	}

}
