package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.config.AndroidManagementConfig;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceRegistrationResponse;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.AndroidManagementFactory;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.ServiceAccountKeyResolver;
import com.antiTheftTracker.antiTheftTrackerApp.utils.mapper.AndroidManagementMapper;
import com.google.api.services.androidmanagement.v1.AndroidManagement;
import com.google.api.services.androidmanagement.v1.model.EnrollmentToken;
import com.google.api.services.androidmanagement.v1.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

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
        logger.info("Starting enrollment link generation...");
        
        var client = factory.getClient();
        logger.info("Android Management client created successfully");
        
        String enterpriseName = getEnterpriseName();
        logger.info("Enterprise name: {}", enterpriseName);

        if (!enterpriseName.startsWith("enterprises/")) {
            throw new IllegalStateException("Invalid enterprise name");
        }

        String policyName = config.getPolicyName();
        logger.info("Policy name: {}", policyName);

        var token = AndroidManagementMapper.mapToDeviceOwnerEnrollmentToken(
                new User().setAccountIdentifier("auto-enroll"),
                enterpriseName,
                policyName
        );
        logger.info("Enrollment token created successfully");

        try {
            logger.info("Checking if enterprise exists: {}", enterpriseName);
            try {
                var enterprise = client.enterprises().get(enterpriseName).execute();
                logger.info("Enterprise found: {}", enterprise.getName());
            } catch (IOException e) {
                logger.error("Enterprise {} does not exist. Error: {}", enterpriseName, e.getMessage());
                throw new IllegalStateException("Enterprise does not exist. Please create an enterprise using the enterprise management endpoints before enrolling devices.");
            }

            logger.info("Creating enrollment token...");
            EnrollmentToken response = client.enterprises()
                    .enrollmentTokens()
                    .create(enterpriseName, token)
                    .execute();
            logger.info("Enrollment token created successfully: {}", response.getName());

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

    public AndroidManagement getAndroidManagementClient() throws IOException {
        return factory.getClient();
    }

    private String getEnterpriseName() {
        Optional<String> enterpriseId = settingService.getSetting("enterpriseId");
        logger.info("Retrieved enterprise ID from settings: {}", enterpriseId.orElse("NOT FOUND"));
        
        return enterpriseId
                .map(id -> "enterprises/" + id)
                .orElseThrow(() -> new IllegalStateException("Enterprise ID not found in settings. Please check if DataInitializationConfig is working."));
    }






}
