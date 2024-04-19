package org.springframework.samples.petclinic.owner.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.owner.OwnerService;

@Configuration
public class OwnerConfig {

	private final OwnerRepository owners;

	public OwnerConfig(OwnerRepository ownerRepository) {
		this.owners = ownerRepository;
	}
	@Bean
	public OwnerService ownerService() {
		return new OwnerServiceImpl(this.owners);
	}
}
