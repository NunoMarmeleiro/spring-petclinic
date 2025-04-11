package org.springframework.samples.petclinic.owners.infrastructure;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CompletableFuture;


@ExtendWith(SpringExtension.class)
@WebMvcTest(OwnerManagement.class)
@ActiveProfiles("test")
public class OwnerManagementTest {
    @Autowired
    private OwnerManagement ownerManagement;

    @MockitoBean
    private KafkaTemplate<String, Integer> kafkaTemplate;

    @Captor
    private ArgumentCaptor<Integer> ownerIdCaptor;

    @Test
    void sendOwnerDeletedShouldSendKafkaMessage() {
        Integer ownerId = 10;

        CompletableFuture<SendResult<String, Integer>> futureMock = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(eq("ownerDeleted"), any(Integer.class))).thenReturn(futureMock);

        ownerManagement.sendOwnerDeleted(ownerId);
        verify(kafkaTemplate, times(1)).send(eq("ownerDeleted"), ownerIdCaptor.capture());

        Integer capturedOwnerId = ownerIdCaptor.getValue();
        assert capturedOwnerId.equals(ownerId);


    }
}
