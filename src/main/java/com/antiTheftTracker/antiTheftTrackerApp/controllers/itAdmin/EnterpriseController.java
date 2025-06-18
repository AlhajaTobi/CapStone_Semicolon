package com.antiTheftTracker.antiTheftTrackerApp.controllers.itAdmin;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.itAdmin.EnterpriseRegistration;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.itAdmin.EnterpriseListResponse;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.itAdmin.SignupUrlResponse;
import com.antiTheftTracker.antiTheftTrackerApp.services.enterpriseManagementServ.EnterpriseService;
import com.google.api.services.androidmanagement.v1.model.Enterprise;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enterprise")
public class EnterpriseController {
    private static final Logger logger = LoggerFactory.getLogger(EnterpriseController.class);
    @Value("${service.account.keys.default-key-id}")
    private String defaultServiceAccountKeyId;
    private final EnterpriseService enterpriseService;



    @GetMapping("/signup-url")
    public ResponseEntity<SignupUrlResponse> getSignupUrl(
            @RequestParam String serviceAccountKeyId,
            @RequestParam(required = false) String callbackUrl) {
        try {
            SignupUrlResponse response = enterpriseService.generateSignupUrl(serviceAccountKeyId, callbackUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Failed to generate signup URL for serviceAccountKeyId: {}", serviceAccountKeyId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/list")
    public List<EnterpriseListResponse> listEnterprises(@RequestParam String serviceAccountKeyId) throws IOException {
        return enterpriseService.listEnterprises(serviceAccountKeyId);
    }

    @PostMapping("/create")
    public ResponseEntity<Enterprise> createEnterprise(
            @RequestParam String serviceAccountKeyId,
            @RequestParam String signupUrlName,
            @RequestParam String enterpriseToken) {
        try {
            Enterprise enterprise = enterpriseService.createEnterprise(serviceAccountKeyId,signupUrlName, enterpriseToken);
            return ResponseEntity.ok(enterprise);
        } catch (IOException e) {
            logger.error("Failed to create enterprise for serviceAccountKeyId: {}", serviceAccountKeyId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam("enterpriseToken") String token,
            @RequestParam("signupUrlName") String urlName,
            @RequestParam(value = "serviceAccountKeyId", defaultValue = "") String serviceAccountKeyId) {
        try {
            String keyId = serviceAccountKeyId.isEmpty() ? defaultServiceAccountKeyId : serviceAccountKeyId;
            Enterprise enterprise = enterpriseService.createEnterprise(keyId,urlName, token);
            return ResponseEntity.ok("Enterprise created successfully: " + enterprise.getName());
        } catch (IOException e) {
            logger.error("Failed to create enterprise for serviceAccountKeyId: {}", serviceAccountKeyId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create enterprise: " + e.getMessage());

        }
    }

    @GetMapping("/currentEnterprise")
    public ResponseEntity<String> getCurrentEnterprise(@RequestParam String projectId, @RequestParam String serviceAccountKeyId) {
        Optional<EnterpriseRegistration> reg = enterpriseService.getEnterpriseRegistration(projectId, serviceAccountKeyId);
        return reg.map(r -> ResponseEntity.ok(r.getEnterpriseName()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No enterprise found"));
    }


}