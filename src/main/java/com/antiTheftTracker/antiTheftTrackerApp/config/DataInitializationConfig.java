package com.antiTheftTracker.antiTheftTrackerApp.config;

import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.SettingService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializationConfig {

    private final SettingService settingService;

    @Value("${service.account.keys.default-key-id}")
    private String defaultServiceAccountKeyId;

    @Value("${android.management.enterprise.id}")
    private String enterpriseId;

    @PostConstruct
    public void initializeSettings() {
        // Initialize serviceAccountKeyId setting
        if (settingService.getSetting("serviceAccountKeyId").isEmpty()) {
            settingService.saveSetting("serviceAccountKeyId", defaultServiceAccountKeyId);
        }

        // Initialize enterpriseId setting
        if (settingService.getSetting("enterpriseId").isEmpty()) {
            settingService.saveSetting("enterpriseId", enterpriseId);
        }
    }
} 