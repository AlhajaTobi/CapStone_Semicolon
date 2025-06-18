package com.antiTheftTracker.antiTheftTrackerApp.services.enterpriseManagementServ;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.itAdmin.EnterpriseRegistration;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.itAdmin.EnterpriseListResponse;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.itAdmin.SignupUrlResponse;

import com.google.api.services.androidmanagement.v1.model.Enterprise;

import java.io.IOException;

import java.util.List;
import java.util.Optional;


public interface EnterpriseService {

    SignupUrlResponse generateSignupUrl(String serviceAccountKeyId, String callbackUrl) throws IOException;

    Enterprise createEnterprise(String serviceAccountKeyId, String signupUrlName, String enterpriseToken) throws IOException;

    List<EnterpriseListResponse> listEnterprises(String serviceAccountKeyId) throws IOException;

    Optional<EnterpriseRegistration> getEnterpriseRegistration(String projectId, String serviceAccountKeyId);


    }