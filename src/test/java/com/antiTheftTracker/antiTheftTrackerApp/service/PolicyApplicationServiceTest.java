package com.antiTheftTracker.antiTheftTrackerApp.service;

import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.PolicyApplicationServiceImpl;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.AndroidManagementFactory;
import com.google.api.services.androidmanagement.v1.AndroidManagement;
import com.google.api.services.androidmanagement.v1.model.Device;
import com.google.api.services.androidmanagement.v1.model.Policy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;


import java.io.IOException;


@ExtendWith(MockitoExtension.class)
public class PolicyApplicationServiceTest {
    @Mock
    private AndroidManagementFactory factory;

    @Mock
    private AndroidManagement mockClient;

    @Mock
    private AndroidManagement.Enterprises mockEnterprises;

    @Mock
    private AndroidManagement.Enterprises.Policies mockPolicies;

    @Mock
    private AndroidManagement.Enterprises.Policies.Patch mockPatchCall;

    @Mock
    private AndroidManagement.Enterprises.Devices mockDevices;

    @Mock
    private AndroidManagement.Enterprises.Devices.Patch mockDevicePatchCall;

    @InjectMocks
    private PolicyApplicationServiceImpl policyApplicationService;

    @BeforeEach
    void setUp() throws IOException {
        when(factory.getClient()).thenReturn(mockClient);
        when(factory.getEnterpriseName()).thenReturn("enterprises/LC11kgdbcd");

        when(mockClient.enterprises()).thenReturn(mockEnterprises);
        lenient().when(mockEnterprises.policies()).thenReturn(mockPolicies);
        lenient().when(mockPolicies.patch(anyString(), any(Policy.class))).thenReturn(mockPatchCall);
        lenient().when(mockPatchCall.execute()).thenReturn(null);

        lenient().when(mockEnterprises.devices()).thenReturn(mockDevices);
        lenient().when(mockDevices.patch(anyString(), any(Device.class))).thenReturn(mockDevicePatchCall);
        lenient().when(mockDevicePatchCall.execute()).thenReturn(null);
    }

    @Test
    void testApplyVaultPolicy_callsPatchCorrectly() throws Exception {
        var policy = mock(Policy.class);

        try (var loader = mockStatic(PolicyApplicationServiceImpl.class)) {
            loader.when(() -> PolicyApplicationServiceImpl.loadPolicyFromResource(anyString()))
                    .thenReturn(policy);

            policyApplicationService.applyVaultPolicy("vault_policy");

            verify(mockPolicies).patch(eq("enterprises/LC11kgdbcd/policies/vault_policy"), eq(policy));
            verify(mockPatchCall).execute();
        }
    }

    @Test
    void testAssignPolicyToDevice_sendsPolicyName() throws Exception {
        String deviceId = "enterprises/LC11kgdbcd/devices/device-123";
        String policyName = "vault_policy";

        policyApplicationService.assignPolicyToDevice(deviceId, policyName);

        verify(mockDevices).patch(eq(deviceId), any(Device.class));
        verify(mockDevicePatchCall).execute();
    }
}