package com.antiTheftTracker.antiTheftTrackerApp.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfiguration {

    @Value("${firebase.credentials-path}")
    private String firebaseCredentialsPath;

    @PostConstruct
    void initializeFirebase() throws IOException {

        try(InputStream serviceAccount = getClass().getResourceAsStream(firebaseCredentialsPath)) {
            if(serviceAccount == null) {
                throw new IOException("Could not find firebase credentials");
            }
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }


}
