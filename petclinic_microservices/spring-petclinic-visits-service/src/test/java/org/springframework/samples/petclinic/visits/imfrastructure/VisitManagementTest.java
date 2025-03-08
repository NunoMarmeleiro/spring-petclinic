package org.springframework.samples.petclinic.visits.imfrastructure;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.samples.petclinic.visits.infrastructure.VisitManagement;
import org.springframework.samples.petclinic.visits.infrastructure.VisitRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VisitManagement.class)
@ActiveProfiles("test")
public class VisitManagementTest {

    @Autowired
    private VisitManagement visitManagement;

    @MockitoBean
    private VisitRepository visitRepository;

    @MockitoBean
    private KafkaTemplate<String, Integer> kafkaTemplate;


    @Test
    void listenPetDeleted_ShouldDeleteVisits() {
        // Given
        Integer petId = 5;
        visitManagement = new VisitManagement(visitRepository, kafkaTemplate);

        // When
        visitManagement.listenPetDeleted(petId);

        // Then
        verify(visitRepository, times(1)).deleteByPetId(petId);
    }
}
