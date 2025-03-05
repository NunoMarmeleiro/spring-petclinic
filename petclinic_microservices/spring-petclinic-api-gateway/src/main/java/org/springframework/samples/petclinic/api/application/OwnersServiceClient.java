package org.springframework.samples.petclinic.api.application;

import org.springframework.samples.petclinic.api.dto.OwnerDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OwnersServiceClient {

    private final WebClient.Builder webClientBuilder;
    // Could be changed for testing purpose
    private String hostname = "http://owners-service/";

    public OwnersServiceClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<OwnerDetails> getOwner(final int ownerId) {
        return webClientBuilder.build().get()
            .uri(hostname + "owners/{ownerId}", ownerId)
            .retrieve()
            .bodyToMono(OwnerDetails.class);
    }

    void setHostname(String hostname) {
        this.hostname = hostname;
    }

}
