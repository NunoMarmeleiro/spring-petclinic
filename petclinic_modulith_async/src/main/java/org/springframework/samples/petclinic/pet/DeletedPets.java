package org.springframework.samples.petclinic.pet;

import org.jmolecules.event.types.DomainEvent;

import java.util.List;

public record DeletedPets(List<Integer> petIds) implements DomainEvent {}
