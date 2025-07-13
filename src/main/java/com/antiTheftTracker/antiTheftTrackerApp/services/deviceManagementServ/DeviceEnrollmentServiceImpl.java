package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.config.AndroidManagementConfig;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceRegistrationResponse;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.AndroidManagementFactory;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.ServiceAccountKeyResolver;
import com.antiTheftTracker.antiTheftTrackerApp.utils.mapper.AndroidManagementMapper;
import com.google.api.services.androidmanagement.v1.model.EnrollmentToken;
import com.google.api.services.androidmanagement.v1.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DeviceEnrollmentServiceImpl implements DeviceEnrollmentService {

    private final Logger logger = LoggerFactory.getLogger(DeviceEnrollmentServiceImpl.class);
    private final AndroidManagementFactory factory;
    private final SettingService settingService;
    private final AndroidManagementConfig config;
    private final ServiceAccountKeyResolver serviceAccountKeyResolver;


    @Override
    public DeviceRegistrationResponse generateEnrollmentLink() throws IOException {
        var client = factory.getClient();
        String enterpriseName = getEnterpriseName();

        if (!enterpriseName.startsWith("enterprises/")) {
            throw new IllegalStateException("Invalid enterprise name");
        }

        String policyName = config.getPolicyName();

        var token = AndroidManagementMapper.mapToDeviceOwnerEnrollmentToken(
                new User().setAccountIdentifier("auto-enroll"),
                enterpriseName,
                policyName
        );

        try {
            EnrollmentToken response = client.enterprises()
                    .enrollmentTokens()
                    .create(enterpriseName, token)
                    .execute();

            return new DeviceRegistrationResponse(
                    response.getName(),
                    response.getValue(),
                    buildEnrollmentLink(response.getValue()),
                    response.getQrCode()
            );

        } catch (IOException error) {
            logger.error("Failed to generate enrollment link", error);
            throw error;
        }
    }

    @Override
    public String buildEnrollmentLink(String tokenId) {
        return "https://play.google.com/managed-devices?token=" + tokenId.trim();
    }

    private String getEnterpriseName() {
        return settingService.getSetting("enterpriseId")
                .map(id -> "enterprises/" + id)
                .orElseThrow(() -> new IllegalStateException("Enterprise ID not found. Please create the enterprise first."));
    }






}
