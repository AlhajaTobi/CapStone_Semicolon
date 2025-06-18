package com.antiTheftTracker.antiTheftTrackerApp.services.enterpriseManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.itAdmin.EnterpriseRegistration;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.itAdmin.EnterpriseRegistrationRepository;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.itAdmin.EnterpriseListResponse;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.itAdmin.SignupUrlResponse;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.ServiceAccountKeyResolver;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidmanagement.v1.AndroidManagement;
import com.google.api.services.androidmanagement.v1.model.Enterprise;
import com.google.api.services.androidmanagement.v1.model.SignupUrl;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnterpriseServiceImpl implements EnterpriseService {

    private final Logger logger = LoggerFactory.getLogger(EnterpriseServiceImpl.class);
    private static final String ANDROID_MANAGEMENT_SCOPE = "https://www.googleapis.com/auth/androidmanagement";

    private final ServiceAccountKeyResolver serviceAccountKeyResolver;

    private final EnterpriseRegistrationRepository enterpriseRepository;

    @Override
    public SignupUrlResponse generateSignupUrl(String serviceAccountKeyId, String callbackUrl) throws IOException {
        logger.info("Generating signup URL for callback: {}, serviceAccountKeyId: {}", callbackUrl, serviceAccountKeyId);

        try (InputStream keyStream = serviceAccountKeyResolver.resolveAsStream(serviceAccountKeyId)) {
            validateIf(serviceAccountKeyId, keyStream);

            GoogleCredentials credentials = GoogleCredentials.fromStream(keyStream)
                    .createScoped(Collections.singletonList(ANDROID_MANAGEMENT_SCOPE));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();
            validate(projectId == null, "Project ID not found in service account credentials", "Project ID not found in credentials");

            AndroidManagement client = getAndroidManagementClient(credentials);
            SignupUrl signupUrl = client.signupUrls()
                    .create()
                    .setProjectId(projectId)
                    .setCallbackUrl(callbackUrl)
                    .execute();
            return new SignupUrlResponse(signupUrl.getName(), signupUrl.getUrl());
        }
    }

    public Enterprise createEnterprise(String serviceAccountKeyId, String signupUrlName, String enterpriseToken) throws IOException {
        logger.info("Creating enterprise with token: {}, serviceAccountKeyId: {}", enterpriseToken, serviceAccountKeyId);

        checkIf(serviceAccountKeyId, "Service account key ID is null or empty", "Service account key ID is required");
        checkIf(enterpriseToken, "Enterprise token is null or empty", "Enterprise token is required");

        try (InputStream keyStream = serviceAccountKeyResolver.resolveAsStream(serviceAccountKeyId)) {
            validateIf(serviceAccountKeyId, keyStream);

            GoogleCredentials credentials = GoogleCredentials.fromStream(keyStream)
                    .createScoped(Collections.singletonList(ANDROID_MANAGEMENT_SCOPE));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();
            validate(projectId == null, "Project ID not found in service account credentials", "Project ID not found in credentials");

            AndroidManagement client = getAndroidManagementClient(credentials);
            Enterprise enterprise = new Enterprise()
                    .setEnterpriseDisplayName("Anti-Theft Tracker Enterprise");

            Optional<EnterpriseRegistration> existing = enterpriseRepository.findByProjectIdAndServiceAccountKeyId(projectId, serviceAccountKeyId);
            if (enterpriseIs(existing)) return getExistingEnterprise(serviceAccountKeyId, existing.get().getEnterpriseName());

            try {
                Enterprise createdEnterprise = client.enterprises()
                        .create(enterprise)
                        .setProjectId(projectId)
                        .setSignupUrlName(signupUrlName)
                        .setEnterpriseToken(enterpriseToken)
                        .execute();
                logger.info("Enterprise created successfully: {}", createdEnterprise.getName());

                String enterpriseName = createdEnterprise.getName();

                EnterpriseRegistration registration = new EnterpriseRegistration();
                registration.setServiceAccountKeyId(serviceAccountKeyId);
                registration.setProjectId(projectId);
                registration.setSignupUrlName(signupUrlName);
                registration.setEnterpriseToken(enterpriseToken);
                registration.setEnterpriseName(enterpriseName);
                registration.setDisplayName(createdEnterprise.getEnterpriseDisplayName());
                registration.setCreatedAt(LocalDateTime.now());

                enterpriseRepository.save(registration);
                return createdEnterprise;
            } catch (IOException e) {
                logger.error("Failed to create enterprise for projectId: {}, signupUrlName: {}", projectId, signupUrlName, e);
                throw new IOException("Failed to create enterprise: " + e.getMessage(), e);
            }
        }
    }



    public List<EnterpriseListResponse> listEnterprises(String serviceAccountKeyId) throws IOException {
        logger.info("Listing enterprises for serviceAccountKeyId: {}", serviceAccountKeyId);

        try (InputStream keyStream = serviceAccountKeyResolver.resolveAsStream(serviceAccountKeyId)) {
            validateIf(serviceAccountKeyId, keyStream);

            GoogleCredentials credentials = GoogleCredentials.fromStream(keyStream)
                    .createScoped(Collections.singletonList(ANDROID_MANAGEMENT_SCOPE));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();
            validate(projectId == null, "Project ID not found in service account credentials", "Project ID not found in credentials");

            AndroidManagement client = getAndroidManagementClient(credentials);
            var response = client.enterprises().list().setProjectId(projectId).execute();
            List<Enterprise> enterprises = response.getEnterprises();

            if (enterprises == null || enterprises.isEmpty()) {
                logger.info("No enterprises found for project ID: {}", projectId);
                return Collections.emptyList();
            }

            return enterprises.stream()
                    .map(e -> new EnterpriseListResponse(e.getName(), e.getEnterpriseDisplayName()))
                    .collect(Collectors.toList());
        }
    }

    public  Optional<EnterpriseRegistration> getEnterpriseRegistration(String projectId, String serviceAccountKeyId) {
        Optional<EnterpriseRegistration> reg = enterpriseRepository.findByProjectIdAndServiceAccountKeyId(projectId, serviceAccountKeyId);
        return reg;
    }

    private AndroidManagement getAndroidManagementClient(GoogleCredentials credentials) throws IOException {
        try {
            logger.info("Creating Android Management client with credentials");
            credentials.refreshIfExpired();

            validate(credentials.getAccessToken() == null, "Failed to obtain access token", "Failed to authenticate with Google API");

            String accessToken = credentials.getAccessToken().getTokenValue();
            return new AndroidManagement.Builder(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    request -> request.getHeaders().setAuthorization("Bearer " + accessToken))
                    .setApplicationName("Anti-Theft Tracker")
                    .build();
        } catch (IOException e) {
            logger.error("Failed to create Android Management client", e);
            throw new IOException("Failed to authenticate with Google API using service account key", e);
        }
    }


    private void checkIf(String serviceAccountKeyId, String s, String s1) {
        if (serviceAccountKeyId == null || serviceAccountKeyId.isEmpty()) {
            logger.error(s);
            throw new IllegalArgumentException(s1);
        }
    }

    private void validate(boolean projectId, String s, String message) throws IOException {
        if (projectId) {
            logger.error(s);
            throw new IOException(message);
        }
    }

    private void validateIf(String serviceAccountKeyId, InputStream keyStream) throws IOException {
        if (keyStream == null) {
            logger.error("Failed to resolve service account key for ID: {}", serviceAccountKeyId);
            throw new IOException("Service account key not found");
        }
    }

    private Enterprise getExistingEnterprise(String serviceAccountId, String enterpriseName) throws IOException {
        try (InputStream keyStream = serviceAccountKeyResolver.resolveAsStream(serviceAccountId)) {
            AndroidManagement client = getAndroidManagementClient(GoogleCredentials.fromStream(keyStream));
            return client.enterprises().get(enterpriseName).execute();
        }
    }

    private boolean enterpriseIs(Optional<EnterpriseRegistration> existing) {
        if (existing.isPresent()) {
            logger.info("Enterprise already exists: {}", existing.get().getEnterpriseName());
            return true;
        }
        return false;
    }

}
