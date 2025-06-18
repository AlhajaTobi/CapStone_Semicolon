package com.antiTheftTracker.antiTheftTrackerApp.utils.mapper;

import com.google.api.services.androidmanagement.v1.model.EnrollmentToken;
import com.google.api.services.androidmanagement.v1.model.User;


public class AndroidManagementMapper {


    public static EnrollmentToken mapToDeviceOwnerEnrollmentToken(
            User user, String enterpriseName, String policyName) {
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        var token = new EnrollmentToken();
        token.setUser(user);
        token.setOneTimeOnly(true);
        token.setPolicyName(String.format("%s/policies/%s", enterpriseName, policyName));

        return token;

    }
}
