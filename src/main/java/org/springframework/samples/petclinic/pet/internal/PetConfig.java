package org.springframework.samples.petclinic.pet.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.owner.internal.OwnerServiceImpl;
import org.springframework.samples.petclinic.pet.PetService;

@Configuration
public class PetConfig {

	private final PetRepository pets;

	public PetConfig(PetRepository petRepo) {
		this.pets = petRepo;
	}
	@Bean
	public PetService ownerService() {
		return new PetServiceImpl(this.pets);
	}
}
