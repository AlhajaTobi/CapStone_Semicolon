package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceEntityResponse;

import java.util.List;
import java.util.Map;

public interface DeviceEntityService {
    DeviceEntityResponse getDeviceMetaData(String deviceId);
    List<DeviceEntityResponse> getAllDevices();
    Map<String, Object> getDeviceLocation(String deviceId);
    void updateDeviceLocation(String deviceId, Double latitude, Double longitude);
}
