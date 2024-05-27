package org.springframework.samples.petclinic.owner;

import org.jmolecules.event.types.DomainEvent;

public record DeletedOwner(int ownerId) implements DomainEvent {}
