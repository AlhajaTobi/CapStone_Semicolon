package com.antiTheftTracker.antiTheftTrackerApp.controller;
import com.antiTheftTracker.antiTheftTrackerApp.controllers.device.DeviceController;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceDetailsResponse;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceEntityResponse;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceRegistrationResponse;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceDetailsService;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceEnrollmentService;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceEnrollmentService deviceEnrollmentService;

    @MockBean
    private DeviceEntityService deviceEntityService;
    @MockBean
    private DeviceDetailsService deviceDetailsService;

    @MockBean
    private DeviceEntityResponse deviceEntityResponse;
    @MockBean
    private DeviceDetailsResponse deviceDetailsResponse;


    @BeforeEach
    void setUp() {
        deviceEntityResponse = new DeviceEntityResponse(
                "device123", "policyXYZ", "2024-01-01T00:00:00Z",
                false, "2024-01-01T12:00:00Z", 6.5244, 3.3792, LocalDateTime.now(),
    "TecnoJ7", "2345678921"
        );

        deviceDetailsResponse = new DeviceDetailsResponse(
                "device123", "Samsung", "SN123456", "123456789012345",
                "6789", "iccid1", "iccid0", "Airtel", "MTN", "08087654321", "09054345677", "", 0.34, 3.56, LocalDateTime.now(), "",
    false
        );
    }


        @Test
        void shouldReturnEnrollmentLink() throws Exception {
            DeviceRegistrationResponse response = new DeviceRegistrationResponse(
                    "enrollmentTokens/test123",
                    "mockedToken",
                    "https://play.google.com/managed-devices?token=mockedToken",
                    "{\\\"android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME\\\":\\\"com.google.android.apps.work.clouddpc/.receivers.CloudDeviceAdminReceiver\\\",\\\"android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM\\\":\\\"I5YvS0O5hXY46mb01BlRjq4oJJGs2kuUcHvVkAPEXlg\\\",\\\"android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION\\\":\\\"https://play.google.com/managed/downloadManagingApp?identifier=setup\\\",\\\"android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE\\\":{\\\"com.google.android.apps.work.clouddpc.EXTRA_ENROLLMENT_TOKEN\\\":\\\"MOCKEDTOKEN\\\"}}"
            );

            when(deviceEnrollmentService.generateEnrollmentLink()).thenReturn(response);

            mockMvc.perform(get("/api/v1/device/enrol"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tokenId").value("enrollmentTokens/test123"))
                    .andExpect(jsonPath("$.tokenValue").value("mockedToken"))
                    .andExpect(jsonPath("$.enrollmentUrl").value("https://play.google.com/managed-devices?token=mockedToken"))
                    .andExpect(jsonPath("$.qrCodeImageUrl").value("{\\\"android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME\\\":\\\"com.google.android.apps.work.clouddpc/.receivers.CloudDeviceAdminReceiver\\\",\\\"android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM\\\":\\\"I5YvS0O5hXY46mb01BlRjq4oJJGs2kuUcHvVkAPEXlg\\\",\\\"android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION\\\":\\\"https://play.google.com/managed/downloadManagingApp?identifier=setup\\\",\\\"android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE\\\":{\\\"com.google.android.apps.work.clouddpc.EXTRA_ENROLLMENT_TOKEN\\\":\\\"MOCKEDTOKEN\\\"}}"));
        }

    @Test
    void testGetDeviceDetails() throws Exception {
        when(deviceDetailsService.getDeviceDetails("device123")).thenReturn(deviceDetailsResponse);

        mockMvc.perform(get("/api/v1/device/details/device123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value("device123"))
                .andExpect(jsonPath("$.manufacturer").value("Samsung"))
                .andExpect(jsonPath("$.deviceSerialNumber").value("SN123456"))
                .andExpect(jsonPath("$.imei").value("123456789012345"))
                .andExpect(jsonPath("$.phoneNumberSlot0").value("08012345678"))
                .andExpect(jsonPath("$.phoneNumberSlot1").value("08087654321"))
                .andExpect(jsonPath("$.carrierSlot0").value("MTN"))
                .andExpect(jsonPath("$.carrierSlot1").value("Airtel"))
                .andExpect(jsonPath("$.simIccidSlot0").value("iccid0"))
                .andExpect(jsonPath("$.simIccidSlot1").value("iccid1"));
    }


    @Test
    void testGetDeviceMetaData() throws Exception {
        when(deviceEntityService.getDeviceMetaData("device123")).thenReturn(deviceEntityResponse);

        mockMvc.perform(get("/api/v1/device/meta-data/device123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("device123"))
                .andExpect(jsonPath("$.policyName").value("policyXYZ"));
    }

        @Test
        void shouldHandleIOException() throws Exception {
            when(deviceEnrollmentService.generateEnrollmentLink()).thenThrow(new IOException("Network error"));

            mockMvc.perform(get("/api/v1/device/enrol"))
                    .andExpect(status().isInternalServerError());
        }
    }

