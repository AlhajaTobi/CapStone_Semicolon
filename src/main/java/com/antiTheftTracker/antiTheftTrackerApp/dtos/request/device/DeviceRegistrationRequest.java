package com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeviceRegistrationRequest {
    private String deviceId;
    private String manufacturer;
    private String deviceModel;
    private String serialNumber;
    private String imei;
    private String simSerialNumber;
    private String carrierName;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private String fcmToken;
}
