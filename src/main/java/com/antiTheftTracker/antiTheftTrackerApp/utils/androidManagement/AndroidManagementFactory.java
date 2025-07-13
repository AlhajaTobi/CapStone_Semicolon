package com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement;

import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.SettingService;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidmanagement.v1.AndroidManagement;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AndroidManagementFactory {

    private final SettingService settingService;
    private final ServiceAccountKeyResolver serviceAccountKeyResolver;


    public AndroidManagement getClient() throws IOException {
        String serviceAccountKeyId = settingService.getSetting("serviceAccountKeyId")
                .orElse("dev"); // Default to "dev" if not set

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountKeyResolver.resolveAsStream(serviceAccountKeyId))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/androidmanagement"));

        HttpRequestInitializer requestInitializer = request -> {
            request.getHeaders().setAuthorization("Bearer " + credentials.refreshAccessToken().getTokenValue());
        };

        return new AndroidManagement.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                requestInitializer
        ).setApplicationName("Anti-Theft Vault App").build();
    }

    public String getEnterpriseName() {
        return settingService.getSetting("enterpriseId")
                .orElseThrow(() -> new IllegalStateException("Enterprise ID not found in settings"));
    }
}
