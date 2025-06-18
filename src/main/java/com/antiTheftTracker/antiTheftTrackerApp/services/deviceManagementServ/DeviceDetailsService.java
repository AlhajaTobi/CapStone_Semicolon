package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceDetailsResponse;

import java.io.IOException;

public interface DeviceDetailsService {
    public DeviceDetailsResponse getDeviceDetails(String deviceId) throws IOException;

}