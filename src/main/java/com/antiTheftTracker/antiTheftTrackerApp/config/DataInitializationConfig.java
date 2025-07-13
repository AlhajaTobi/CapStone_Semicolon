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
        System.out.println("DataInitializationConfig: Starting settings initialization...");
        System.out.println("DataInitializationConfig: defaultServiceAccountKeyId = " + defaultServiceAccountKeyId);
        System.out.println("DataInitializationConfig: enterpriseId = " + enterpriseId);
        
        // Initialize serviceAccountKeyId setting
        if (settingService.getSetting("serviceAccountKeyId").isEmpty()) {
            System.out.println("DataInitializationConfig: Saving serviceAccountKeyId = " + defaultServiceAccountKeyId);
            settingService.saveSetting("serviceAccountKeyId", defaultServiceAccountKeyId);
        } else {
            System.out.println("DataInitializationConfig: serviceAccountKeyId already exists");
        }

        // Initialize enterpriseId setting
        if (settingService.getSetting("enterpriseId").isEmpty()) {
            System.out.println("DataInitializationConfig: Saving enterpriseId = " + enterpriseId);
            settingService.saveSetting("enterpriseId", enterpriseId);
        } else {
            System.out.println("DataInitializationConfig: enterpriseId already exists");
        }
        
        System.out.println("DataInitializationConfig: Settings initialization completed");
    }
} 