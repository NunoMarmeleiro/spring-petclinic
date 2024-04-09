package org.springframework.samples.petclinic.owner.internal;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerService;
import org.springframework.samples.petclinic.owner.internal.OwnerRepository;

public class OwnerServiceImpl implements OwnerService {
	private final OwnerRepository owners;
	public OwnerServiceImpl(OwnerRepository ownerRepo) {
		this.owners = ownerRepo;
	}
	@Override
	public Owner findOwner(int ownerId) {
		return this.owners.findById(ownerId);
	}
}
