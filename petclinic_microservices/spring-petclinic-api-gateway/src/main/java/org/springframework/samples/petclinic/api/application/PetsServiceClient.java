package org.springframework.samples.petclinic.api.application;

import org.springframework.samples.petclinic.api.dto.Pets;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Component
public class PetsServiceClient {

    // Could be changed for testing purpose
    private String hostname = "http://pets-service/";

    private final WebClient.Builder webClientBuilder;

    public PetsServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Pets> getPetsForOwner(final Integer ownerId) {
        return webClientBuilder.build()
            .get()
            .uri(hostname + "/owners/{ownerId}/pets", ownerId)
            .retrieve()
            .bodyToMono(Pets.class);
    }


    void setHostname(String hostname) {
        this.hostname = hostname;
    }

}
