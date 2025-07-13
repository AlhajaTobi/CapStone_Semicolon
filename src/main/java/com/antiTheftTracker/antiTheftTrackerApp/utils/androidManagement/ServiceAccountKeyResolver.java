package com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;

import java.io.IOException;

@Component
public class ServiceAccountKeyResolver {

    private static final Logger logger = LoggerFactory.getLogger(ServiceAccountKeyResolver.class);

    @Value("${service.account.keys.base-path}")
    private String basePath;

    private final ResourceLoader resourceLoader;

    public ServiceAccountKeyResolver(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public InputStream resolveAsStream(String keyId) throws IOException {
        if (keyId == null || keyId.isEmpty()) {
            logger.error("Service account key ID is null or empty");
            throw new IllegalArgumentException("Service account key ID is required");
        }

        if (!keyId.matches("[a-zA-Z0-9_]+")) {
            logger.warn("Invalid key ID format: {}", keyId);
            throw new IllegalArgumentException("Invalid service account key ID: must contain only letters, numbers, or underscores");
        }

        String filename = getKeyFileName(keyId);
        String location = basePath.endsWith("/") ? basePath + filename : basePath + "/" + filename;

        logger.info("Attempting to load service account key from: {}", location);

        try {
            Resource resource = resourceLoader.getResource(location);
            InputStream inputStream = resource.getInputStream();
            logger.info("Successfully loaded service account key: {}", location);
            return inputStream;
        } catch (IOException e) {
            logger.error("Failed to load service account key from: {}", location, e);
            throw new IOException("Failed to load service account key: " + location, e);
        }
    }

    private String getKeyFileName(String keyId) {
        return switch (keyId) {
            case "dev" -> "service-account.json";
            case "prod" -> "prod_service_account.json";
            default -> keyId + ".json";
        };
    }

}



//its purpose is to load Google service account key files (JSON files containing credentials) based on a provided keyId

