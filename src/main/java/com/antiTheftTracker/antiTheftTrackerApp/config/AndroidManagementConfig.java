package com.antiTheftTracker.antiTheftTrackerApp.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AndroidManagementConfig {

    @Value("${android.management.enterprise.id}")
    private String enterpriseId;
    @Value("${android.management.vault.policy.name}")
    private String policyName;
    @Value("${android.management.vault.package}")
    private String packageName;
}
