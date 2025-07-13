package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceRegistrationResponse;
import com.google.api.services.androidmanagement.v1.AndroidManagement;

import java.io.IOException;



public interface DeviceEnrollmentService {


    public DeviceRegistrationResponse generateEnrollmentLink() throws IOException;

    public String buildEnrollmentLink(String tokenId);

    public AndroidManagement getAndroidManagementClient() throws IOException;

}