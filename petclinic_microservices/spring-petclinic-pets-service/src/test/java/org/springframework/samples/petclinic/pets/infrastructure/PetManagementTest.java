package org.springframework.samples.petclinic.pets.infrastructure;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.samples.petclinic.pets.domain.Pet;
import org.springframework.samples.petclinic.pets.domain.PetType;
import org.springframework.samples.petclinic.pets.infrastructure.PetRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PetManagement.class)
@ActiveProfiles("test")
class PetManagementTest {

    @MockitoBean
    private PetRepository petRepository;

    @MockitoBean
    private KafkaTemplate<String, Integer> kafkaTemplate;

    @Autowired
    private PetManagement petManagement;

    @Captor
    private ArgumentCaptor<Integer> petIdCaptor;


    @Test
    void listenOwnerDeleted_ShouldDeletePetsAndSendEvents() throws ParseException {
        Integer ownerId = 1;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        //Pet 1
        Pet pet1 = new Pet();
        PetType petType1 = new PetType();
        petType1.setId(1);
        petType1.setName("Cat");
        pet1.setId(1);
        pet1.setOwnerId(ownerId);
        pet1.setName("Jeremias");
        pet1.setBirthDate(formatter.parse("10-10-2010"));
        pet1.setType(petType1);

        //Pet 2
        Pet pet2 = new Pet();
        PetType petType2 = new PetType();
        petType2.setId(1);
        petType2.setName("Dog");
        pet2.setId(2);
        pet2.setOwnerId(ownerId);
        pet2.setName("Alfredo");
        pet2.setBirthDate(formatter.parse("11-11-2011"));
        pet2.setType(petType2);

        List<Pet> pets = List.of(pet1,pet2);

        when(petRepository.findByOwnerId(ownerId)).thenReturn(pets);
        doNothing().when(petRepository).deleteByOwnerId(ownerId);

        CompletableFuture futureMock = mock(CompletableFuture.class);
        when(kafkaTemplate.send(eq("petDeleted"), any(Integer.class))).thenReturn(futureMock);

        petManagement.listenOwnerDeleted(ownerId);

        verify(petRepository).deleteByOwnerId(ownerId);
        verify(kafkaTemplate, times(2)).send(eq("petDeleted"), petIdCaptor.capture());

        List<Integer> capturedIds = petIdCaptor.getAllValues();
        assert capturedIds.contains(1);
        assert capturedIds.contains(2);
    }

    @Test
    void sendPetDeleted_ShouldLogMessageOnSuccess() throws ExecutionException, InterruptedException {
        Integer petId = 1;
        CompletableFuture futureMock = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send("petDeleted", petId)).thenReturn(futureMock);

        petManagement.sendPetDeleted(petId);

        verify(kafkaTemplate).send("petDeleted", petId);
    }

    @Test
    void sendPetDeleted_ShouldLogErrorOnFailure() {
        Integer petId = 1;
        CompletableFuture futureMock = new CompletableFuture<>();
        futureMock.completeExceptionally(new RuntimeException("Kafka failure"));

        when(kafkaTemplate.send("petDeleted", petId)).thenReturn(futureMock);

        petManagement.sendPetDeleted(petId);

        verify(kafkaTemplate).send("petDeleted", petId);
    }
}
