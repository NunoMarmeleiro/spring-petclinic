package org.springframework.samples.petclinic.pet.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.pet.PetService;

@Configuration
public class PetConfig {

	private final PetRepository pets;

	public PetConfig(PetRepository petRepo) {
		this.pets = petRepo;
	}
	@Bean
	public PetService petService() {
		return new PetServiceImpl(this.pets);
	}
}
