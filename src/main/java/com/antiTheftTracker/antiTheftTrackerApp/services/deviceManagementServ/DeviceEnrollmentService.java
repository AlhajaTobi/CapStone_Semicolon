package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceRegistrationResponse;

import java.io.IOException;



public interface DeviceEnrollmentService {


    public DeviceRegistrationResponse generateEnrollmentLink() throws IOException;

    public String buildEnrollmentLink(String tokenId);

    }