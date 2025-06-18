package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device.DeviceLocationRequest;

public interface DeviceLocationService {
    void updateDeviceLocation(String deviceId, DeviceLocationRequest request);
}
