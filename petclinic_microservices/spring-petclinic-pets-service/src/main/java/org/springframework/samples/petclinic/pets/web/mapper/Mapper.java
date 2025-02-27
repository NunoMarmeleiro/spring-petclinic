package org.springframework.samples.petclinic.pets.web.mapper;

public interface Mapper<R, E> {
    E map(E response, R request);
}
