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
package org.springframework.samples.petclinic.api.boundary.web;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.samples.petclinic.api.application.OwnersServiceClient;
import org.springframework.samples.petclinic.api.application.PetsServiceClient;
import org.springframework.samples.petclinic.api.application.VisitsServiceClient;
import org.springframework.samples.petclinic.api.dto.OwnerDetails;
import org.springframework.samples.petclinic.api.dto.Visits;
import org.springframework.samples.petclinic.api.dto.Pets;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

/**
 * @author Maciej Szarlinski
 */
@RestController
@RequestMapping("/api/gateway")
public class ApiGatewayController {

    //private final CustomersServiceClient customersServiceClient;

    private final VisitsServiceClient visitsServiceClient;
    private final PetsServiceClient petsServiceClient;
    private final OwnersServiceClient ownersServiceClient;

    private final ReactiveCircuitBreakerFactory cbFactory;

    public ApiGatewayController(//CustomersServiceClient customersServiceClient,
                                VisitsServiceClient visitsServiceClient,
                                OwnersServiceClient ownersServiceClient,
                                PetsServiceClient petsServiceClient,
                                ReactiveCircuitBreakerFactory cbFactory) {
        //this.customersServiceClient = customersServiceClient;
        this.visitsServiceClient = visitsServiceClient;
        this.cbFactory = cbFactory;
        this.ownersServiceClient = ownersServiceClient;
        this.petsServiceClient = petsServiceClient;
    }

    /*
    @GetMapping(value = "owners/{ownerId}")
    public Mono<OwnerDetails> getOwnerDetails(final @PathVariable int ownerId) {
        return customersServiceClient.getOwner(ownerId)
            .flatMap(owner ->
                visitsServiceClient.getVisitsForPets(owner.getPetIds())
                    .transform(it -> {
                        ReactiveCircuitBreaker cb = cbFactory.create("getOwnerDetails");
                        return cb.run(it, throwable -> emptyVisitsForPets());
                    })
                    .map(addVisitsToOwner(owner))
            );

    }



    */

    @GetMapping(value = "owners/{ownerId}")
    public Mono<OwnerDetails> getOwnerDetails(final @PathVariable int ownerId) {
        Mono<OwnerDetails> result = ownersServiceClient.getOwner(ownerId)
            .flatMap(owner ->
                petsServiceClient.getPetsForOwner(ownerId).transform(it -> {
                        ReactiveCircuitBreaker cb = cbFactory.create("getOwnerDetails");
                        return cb.run(it, throwable -> emptyPetsForOwner());
                    })
                    .map(addPetsToOwner(owner))
                    .flatMap(pets ->
                        visitsServiceClient.getVisitsForPets(pets.getPetIds())
                            .transform(it -> {
                                ReactiveCircuitBreaker cb = cbFactory.create("getOwnerDetails");
                                return cb.run(it, throwable -> emptyVisitsForPets());
                            })
                            .map(addVisitsToOwner(owner))
                    )

            );
        return result;
    }
    private Function<Pets, OwnerDetails> addPetsToOwner(OwnerDetails owner){
        return pets  -> {
            owner.pets().addAll(pets.items().stream().toList());
            return owner;
        };
    }

    private Function<Visits, OwnerDetails> addVisitsToOwner(OwnerDetails owner) {
        return visits -> {
            owner.pets()
                .forEach(pet -> pet.visits()
                    .addAll(visits.items().stream()
                        .filter(v -> v.petId() == pet.id())
                        .toList())
                );
            return owner;
        };
    }

    private Mono<Visits> emptyVisitsForPets() {
        return Mono.just(new Visits(List.of()));
    }

    private Mono<Pets> emptyPetsForOwner() {
        return Mono.just(new Pets(List.of()));
    }
}
