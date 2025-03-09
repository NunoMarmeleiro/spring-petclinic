/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.pets.infrastructure;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.pets.dto.PetDetails;
import org.springframework.samples.petclinic.pets.dto.PetRequest;
import org.springframework.samples.petclinic.pets.domain.Pet;
import org.springframework.samples.petclinic.pets.domain.PetType;
import org.springframework.samples.petclinic.pets.infrastructure.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Maciej Szarlinski
 * @author Ramazan Sakin
 */
@RestController
@Timed("petclinic.pet")
public class PetController {

    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    private final PetRepository petRepository;
    private final PetManagement petManagement;

    PetController(PetRepository petRepository, PetManagement petManagement) {
        this.petRepository = petRepository;
        this.petManagement = petManagement;
    }

    @GetMapping("/petTypes")
    public List<PetType> getPetTypes() {
        return petRepository.findPetTypes();
    }

    @PostMapping("/owners/{ownerId}/pets")
    @ResponseStatus(HttpStatus.CREATED)
    public Pet processCreationForm(
        @RequestBody PetRequest petRequest,
        @PathVariable("ownerId") @Min(1) Integer ownerId) {

        //TODO: Do an async or sync verification to owner service to check if exists
        //Owner owner = ownerRepository.findById(ownerId)
         //   .orElseThrow(() -> new ResourceNotFoundException("Owner " + ownerId + " not found"));

        final Pet pet = new Pet();
        return save(pet, petRequest,ownerId);
    }

    @GetMapping("/owners/{ownerId}/pets")
    @ResponseStatus(HttpStatus.OK)
    public List<Pet> getPetsByOwnerId(
        @PathVariable("ownerId") @Min(1) Integer ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    @PutMapping("/owners/{ownerId}/pets/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void processUpdateForm(@RequestBody PetRequest petRequest,@PathVariable("ownerId") @Min(1) Integer ownerId) {
        int petId = petRequest.id();
        Pet pet = findPetById(petId);
        save(pet, petRequest, ownerId);
    }

    private Pet save(final Pet pet, final PetRequest petRequest, Integer ownerId) {

        pet.setName(petRequest.name());
        pet.setBirthDate(petRequest.birthDate());
        pet.setOwnerId(ownerId);

        petRepository.findPetTypeById(petRequest.typeId())
            .ifPresent(pet::setType);

        log.info("Saving pet {}", pet);
        return petRepository.save(pet);
    }

    @GetMapping("owners/*/pets/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PetDetails> findPet(@PathVariable("petId") int petId) {
        try {
            Pet pet = findPetById(petId);
            return ResponseEntity.ok(new PetDetails(pet));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("pets")
    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    private Pet findPetById(int petId) {
        return petRepository.findById(petId)
            .orElseThrow(() -> new ResourceNotFoundException("Pet " + petId + " not found"));
    }

    @DeleteMapping("owners/{ownerId}/pets/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable("ownerId") Integer ownerId, @PathVariable("petId") int petId) {
        petRepository.deleteById(petId);
        petManagement.sendPetDeleted(petId);
    }




}
