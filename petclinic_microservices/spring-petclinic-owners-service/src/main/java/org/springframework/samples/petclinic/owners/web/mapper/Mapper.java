package org.springframework.samples.petclinic.owners.web.mapper;

public interface Mapper<R, E> {
    E map(E response, R request);
}
