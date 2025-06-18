package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.AndroidManagementFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidmanagement.v1.model.Device;
import com.google.api.services.androidmanagement.v1.model.Policy;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@AllArgsConstructor
public class PolicyApplicationServiceImpl implements PolicyApplicationService {

    private final AndroidManagementFactory factory;

    @Override
    public void applyVaultPolicy(String policyName) throws IOException {
        var policy = loadPolicyFromResource("policies/vault_policy.json");

        var client = factory.getClient();
        String enterpriseName = factory.getEnterpriseName();

        client.enterprises().policies()
                .patch(enterpriseName + "/policies/" + policyName, policy)
                .execute();
    }

    @Override
    public void assignPolicyToDevice(String deviceId, String policyName) throws IOException {
        var management = factory.getClient();
        var device = new Device();
        device.setPolicyName(factory.getEnterpriseName() + "/policies/" + policyName);

        management.enterprises()
                .devices()
                .patch(deviceId, device)
                .execute();
    }

    public static Policy loadPolicyFromResource(String resourcePath) throws IOException {
        try (InputStream inputStream = PolicyApplicationServiceImpl.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Policy file not found: " + resourcePath);
            }
            return new GsonFactory().fromInputStream(inputStream, Policy.class);
        }
    }
}