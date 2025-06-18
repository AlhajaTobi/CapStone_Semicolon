package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device.DeviceRegistrationRequest;

public interface DeviceMetadataRegistration {
    void registerDeviceMetadata(String deviceId, DeviceRegistrationRequest request);

    void updateImei(String deviceId, String imei);

    void updatePhoneNumber(String deviceId, String phoneNumber);
}
