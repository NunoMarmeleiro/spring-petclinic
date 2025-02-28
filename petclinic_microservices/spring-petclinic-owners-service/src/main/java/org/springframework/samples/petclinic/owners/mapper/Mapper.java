package org.springframework.samples.petclinic.owners.mapper;

public interface Mapper<R, E> {
    E map(E response, R request);
}
