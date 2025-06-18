package com.antiTheftTracker.antiTheftTrackerApp.controller;
import com.antiTheftTracker.antiTheftTrackerApp.controllers.device.DeviceLocationController;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device.DeviceLocationRequest;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceLocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DeviceLocationController.class)
public class DeviceLocationControllerTest {
        @Autowired
        private MockMvc mockMvc;


    @Captor
    private ArgumentCaptor<DeviceLocationRequest> locationRequestCaptor;

    @Captor
    DeviceLocationService locationService;

    @Test
    void shouldUpdateDeviceLocation_withArgumentCaptor() throws Exception {
        String deviceId = "samsung123";
        Double latitude = 6.5244;
        Double longitude = 3.3792;

        DeviceLocationRequest request = new DeviceLocationRequest(deviceId, latitude, longitude);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/device/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(locationService, times(1)).updateDeviceLocation(eq(deviceId), locationRequestCaptor.capture());

        DeviceLocationRequest capturedRequest = locationRequestCaptor.getValue();
        assertEquals(latitude, capturedRequest.getLatitude());
        assertEquals(longitude, capturedRequest.getLongitude());
    }
    }


