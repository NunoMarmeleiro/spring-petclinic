package org.springframework.samples.petclinic.owners.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.owners.domain.Owner;
import org.springframework.samples.petclinic.owners.dto.OwnerRequest;
import org.springframework.samples.petclinic.owners.mapper.OwnerEntityMapper;
import org.springframework.samples.petclinic.owners.infrastructure.OwnerManagement;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OwnerController.class)
@ActiveProfiles("test")
class OwnersControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    OwnerRepository ownerRepository;

    @MockBean
    OwnerEntityMapper ownerEntityMapper;

    @MockBean
    OwnerManagement ownerManagement;

    @Test
    void shouldCreateOwnerSuccessfully() throws Exception {
        OwnerRequest ownerRequest = new OwnerRequest("John", "Doe", "123 Street", "City", "123456789");
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");

        given(ownerEntityMapper.map(any(Owner.class), eq(ownerRequest))).willReturn(owner);
        given(ownerRepository.save(any(Owner.class))).willReturn(owner);

        mvc.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "John",
                          "lastName": "Doe",
                          "address": "123 Street",
                          "city": "City",
                          "telephone": "123456789"
                        }
                        """))
            .andExpect(status().isCreated());
    }

    @Test
    void shouldGetSingleOwner() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");

        given(ownerRepository.findById(1)).willReturn(Optional.of(owner));

        mvc.perform(get("/owners/1").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldGetAllOwners() throws Exception {
        Owner owner1 = new Owner();
        owner1.setId(1);
        owner1.setFirstName("John");
        owner1.setLastName("Doe");

        Owner owner2 = new Owner();
        owner2.setId(2);
        owner2.setFirstName("Jane");
        owner2.setLastName("Doe");

        given(ownerRepository.findAll()).willReturn(List.of(owner1, owner2));

        mvc.perform(get("/owners").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void shouldUpdateOwnerSuccessfully() throws Exception {
        OwnerRequest ownerRequest = new OwnerRequest("John", "Updated", "456 Avenue", "NewCity", "987654321");
        Owner existingOwner = new Owner();
        existingOwner.setId(1);
        existingOwner.setFirstName("John");
        existingOwner.setLastName("Doe");

        given(ownerRepository.findById(1)).willReturn(Optional.of(existingOwner));
        given(ownerRepository.save(any(Owner.class))).willReturn(existingOwner);

        mvc.perform(put("/owners/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "firstName": "John",
                          "lastName": "Updated",
                          "address": "456 Avenue",
                          "city": "NewCity",
                          "telephone": "987654321"
                        }
                        """))
            .andExpect(status().isNoContent());

        verify(ownerEntityMapper).map(existingOwner, ownerRequest);
        verify(ownerRepository).save(existingOwner);
    }

    @Test
    void shouldDeleteOwnerSuccessfully() throws Exception {
        int ownerId = 1;

        doNothing().when(ownerRepository).deleteById(ownerId);
        doNothing().when(ownerManagement).sendOwnerDeleted(ownerId);

        mvc.perform(delete("/owners/{ownerId}", ownerId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(ownerRepository).deleteById(ownerId);
        verify(ownerManagement).sendOwnerDeleted(ownerId);
    }
}
