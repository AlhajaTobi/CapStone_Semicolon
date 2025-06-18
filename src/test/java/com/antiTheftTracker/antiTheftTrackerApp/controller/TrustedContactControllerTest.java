package com.antiTheftTracker.antiTheftTrackerApp.controller;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.contact.TrustedContactRepository;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.contact.TrustedContactRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TrustedContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceEntityRepository deviceRepo;

    @Autowired
    private TrustedContactRepository contactRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private DeviceEntity device;

    @BeforeEach
    void setUp() {
        contactRepo.deleteAll();
        deviceRepo.deleteAll();

        device = new DeviceEntity();
        device.setId("Test Device");
        device = deviceRepo.save(device);
    }

    private TrustedContactRequest buildRequest(String name, String email, String phone) {
        TrustedContactRequest request = new TrustedContactRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPhoneNumber(phone);
        request.setSmsEnabled(true);
        request.setEmailEnabled(true);
        return request;
    }

    @Test
    void testAddTrustedContact_returnsAccepted() throws Exception {
        TrustedContactRequest request = buildRequest("John Doe", "john@example.com", "08012345678");

        mockMvc.perform(post("/api/v1/trusted-contact/{deviceId}/contacts", device.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
    }

    @Test
    void testAddTrustedContact_invalidDevice_returnsBadRequest() throws Exception {
        TrustedContactRequest request = buildRequest("Invalid", "invalid@example.com", "000");

        mockMvc.perform(post("/api/v1/trusted-contact/{deviceId}/contacts", "non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTrustedContacts_returnsOkAndList() throws Exception {
        TrustedContactRequest request = buildRequest("Ada", "ada@example.com", "08088888888");
        mockMvc.perform(post("/api/v1/trusted-contact/{deviceId}/contacts", device.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/v1/trusted-contact/{deviceId}", device.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Ada"));
    }

    @Test
    void testDeleteTrustedContact_returnsAccepted() throws Exception {
        TrustedContactRequest request = buildRequest("ToDelete", "del@example.com", "08099999999");
        mockMvc.perform(post("/api/v1/trusted-contact/{deviceId}/contacts", device.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        String contactId = contactRepo.findAll().get(0).getId();

        mockMvc.perform(delete("/api/v1/trusted-contact/{deviceId}/{contactId}", contactId, device.getId()))
                .andExpect(status().isAccepted());
    }

    @Test
    void testDeleteInvalidContact_returnsBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/trusted-contact/{contactId}", "non-existent-id"))
                .andExpect(status().isBadRequest());
    }
}
