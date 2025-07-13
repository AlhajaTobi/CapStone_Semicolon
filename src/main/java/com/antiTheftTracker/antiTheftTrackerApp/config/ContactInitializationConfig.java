package com.antiTheftTracker.antiTheftTrackerApp.config;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.contact.TrustedContactRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ContactInitializationConfig {

    private final TrustedContactRepository trustedContactRepository;

    @PostConstruct
    public void initializeContactSettings() {
        List<TrustedContact> contacts = trustedContactRepository.findAll();
        
        for (TrustedContact contact : contacts) {
            boolean needsUpdate = false;
            
            // Enable email notifications if disabled
            if (!contact.isEmailEnabled()) {
                contact.setEmailEnabled(true);
                needsUpdate = true;
            }
            
            // Enable SMS notifications if disabled
            if (!contact.isSmsEnabled()) {
                contact.setSmsEnabled(true);
                needsUpdate = true;
            }
            
            if (needsUpdate) {
                trustedContactRepository.save(contact);
                System.out.println("Updated contact '" + contact.getName() + "' to enable notifications");
            }
        }
    }
} 