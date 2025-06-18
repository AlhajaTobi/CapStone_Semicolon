package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceEntityResponse;

public interface DeviceEntityService {
    public DeviceEntityResponse getDeviceMetaData(String deviceId);

}
