package org.springframework.samples.petclinic.pets.mapper;

public interface Mapper<R, E> {
    E map(E response, R request);
}
