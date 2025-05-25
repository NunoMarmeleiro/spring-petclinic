package org.springframework.samples.petclinic.owner.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.samples.petclinic.owner.DeletedOwner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerManagement {

	private final ApplicationEventPublisher events;

	@Transactional
	@Async
	public void deleteOwner(int ownerId) {
		events.publishEvent(new DeletedOwner(ownerId));
	}

}
