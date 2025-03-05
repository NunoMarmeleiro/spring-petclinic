package org.springframework.samples.petclinic.api.dto;

import java.util.ArrayList;
import java.util.List;

public record Pets(
    List<PetDetails> items
) {
    public Pets(){
        this(new ArrayList<>());
    }

}
