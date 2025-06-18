package com.antiTheftTracker.antiTheftTrackerApp.service;

import com.antiTheftTracker.antiTheftTrackerApp.config.AndroidManagementConfig;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceEnrollmentServiceImpl;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.PolicyApplicationService;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.AndroidManagementFactory;
import com.antiTheftTracker.antiTheftTrackerApp.utils.mapper.AndroidManagementMapper;
import com.google.api.services.androidmanagement.v1.AndroidManagement;
import com.google.api.services.androidmanagement.v1.model.EnrollmentToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceEnrollmentServiceTest {


    @Mock
    private AndroidManagementFactory factory;

    @Mock
    private AndroidManagementConfig config;


    @Mock
    private PolicyApplicationService policyApplicationService;

    @InjectMocks
    private DeviceEnrollmentServiceImpl enrollmentService;

    private AndroidManagement mockClient;
    private AndroidManagement.Enterprises mockEnterprises;
    private AndroidManagement.Enterprises.EnrollmentTokens mockEnrollmentTokens;
    private AndroidManagement.Enterprises.EnrollmentTokens.Create mockCreateCall;
    private EnrollmentToken mockTokenResponse;



    @BeforeEach
    void setUp() throws IOException {

        lenient().when(config.getPolicyName()).thenReturn("vault_policy");

        mockClient = mock(AndroidManagement.class);
        mockEnterprises = mock(AndroidManagement.Enterprises.class);
        mockEnrollmentTokens = mock(AndroidManagement.Enterprises.EnrollmentTokens.class);
        mockCreateCall = mock(AndroidManagement.Enterprises.EnrollmentTokens.Create.class);

        lenient().when(mockClient.enterprises()).thenReturn(mockEnterprises);
        lenient().when(mockEnterprises.enrollmentTokens()).thenReturn(mockEnrollmentTokens);
        lenient().when(mockEnrollmentTokens.create(anyString(), any(EnrollmentToken.class))).thenReturn(mockCreateCall);

        mockTokenResponse = new EnrollmentToken();
        mockTokenResponse.setValue("TEST_TOKEN_1234567890");
        mockTokenResponse.setName("enrollmentTokens/TOKEN_ID");
        mockTokenResponse.setPolicyName("enterprises/LC11kgdbcd/policies/vault_policy");

        lenient().when(mockCreateCall.execute()).thenReturn(mockTokenResponse);

        lenient().when(factory.getClient()).thenReturn(mockClient);
        lenient().when(factory.getEnterpriseName()).thenReturn("enterprises/LC11kgdbcd");

    }

    @Test
    void testGenerateEnrollmentLink_withValidInput_returnsValidResponse() throws Exception {

        var result = enrollmentService.generateEnrollmentLink();

        assertNotNull(result);
        assertEquals("TEST_TOKEN_1234567890", result.getTokenValue());
        assertTrue(result.getEnrollmentUrl().contains("TEST_TOKEN_1234567890"));
        verify(policyApplicationService).assignPolicyToDevice(anyString(), eq("vault_policy"));

    }



    @Test
    void testGenerateEnrollmentLink_whenCreateTokenFails_throwsIOException() throws Exception {


        var mockClient = mock(AndroidManagement.class);
        var mockEnterprises = mock(AndroidManagement.Enterprises.class);
        var mockEnrollmentTokens = mock(AndroidManagement.Enterprises.EnrollmentTokens.class);
        var mockCreateCall = mock(AndroidManagement.Enterprises.EnrollmentTokens.Create.class);

        when(factory.getClient()).thenReturn(mockClient);
        when(mockClient.enterprises()).thenReturn(mockEnterprises);
        when(mockEnterprises.enrollmentTokens()).thenReturn(mockEnrollmentTokens);
        when(mockEnrollmentTokens.create(anyString(), any(EnrollmentToken.class))).thenReturn(mockCreateCall);

        when(mockCreateCall.execute()).thenThrow(new IOException("Google API error"));

        assertThrows(IOException.class, () -> {
            enrollmentService.generateEnrollmentLink();
        });
    }

    @Test
    void testGenerateEnrollmentLink_whenReflectionFails_throwsRuntimeException(){
        doThrow(new RuntimeException("Method not found"))
                .when(mock(AndroidManagementMapper.class));

        assertThrows(RuntimeException.class, () -> {
            enrollmentService.generateEnrollmentLink();
        });
    }

    @Test
    void testGenerateEnrollmentLink_whenEnterpriseNameNotAvailable_throwsIOException() throws Exception {
        when(factory.getEnterpriseName()).thenReturn(null);

        assertThrows(IOException.class, () -> {
            enrollmentService.generateEnrollmentLink();
        });

    }
}

