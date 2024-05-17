package org.springframework.samples.petclinic.pet;

import org.jmolecules.event.types.DomainEvent;

public record DeletedOwner(int ownerId) implements DomainEvent {
}
