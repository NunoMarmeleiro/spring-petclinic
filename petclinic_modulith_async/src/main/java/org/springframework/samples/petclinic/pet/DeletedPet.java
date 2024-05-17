package org.springframework.samples.petclinic.pet;

import org.jmolecules.event.types.DomainEvent;

public record DeletedPet(int petId) implements DomainEvent {
}
