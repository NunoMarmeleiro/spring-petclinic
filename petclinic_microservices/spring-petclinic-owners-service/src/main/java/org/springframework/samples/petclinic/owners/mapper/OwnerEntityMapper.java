package org.springframework.samples.petclinic.owners.mapper;

import org.springframework.samples.petclinic.owners.domain.Owner;
import org.springframework.samples.petclinic.owners.dto.OwnerRequest;
import org.springframework.stereotype.Component;

@Component
public class OwnerEntityMapper implements Mapper<OwnerRequest, Owner> {
    // This is done by hand for simplicity purpose. In a real life use-case we should consider using MapStruct.
    @Override
    public Owner map(final Owner owner, final OwnerRequest request) {
        owner.setAddress(request.address());
        owner.setCity(request.city());
        owner.setTelephone(request.telephone());
        owner.setFirstName(request.firstName());
        owner.setLastName(request.lastName());
        return owner;
    }
}
