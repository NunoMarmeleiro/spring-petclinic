package org.springframework.samples.petclinic.owner.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.samples.petclinic.owner.DeletedOwner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerManagement {

	private final ApplicationEventPublisher events;

	@Transactional
	public void deleteOwner(int ownerId) {
		events.publishEvent(new DeletedOwner(ownerId));
	}

}
