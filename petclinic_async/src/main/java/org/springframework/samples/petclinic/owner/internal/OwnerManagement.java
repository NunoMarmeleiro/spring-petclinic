package org.springframework.samples.petclinic.owner.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.samples.petclinic.pet.DeletedOwner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class OwnerManagement {

	private final ApplicationEventPublisher events;

	@Transactional
	public void complete(int ownerId) {
		events.publishEvent(new DeletedOwner(ownerId));
	}
}
