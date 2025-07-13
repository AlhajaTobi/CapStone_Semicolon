package com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeviceDetailsResponse {
    private String deviceId;
    private String manufacturer;
    private String deviceModel;
    private String deviceSerialNumber;
    private String imei;
    private String simIccidSlot0;
    private String simIccidSlot1;
    private String carrierSlot0;
    private String carrierSlot1;
    private String phoneNumberSlot0;
    private String phoneNumberSlot1;
    private String policyName;
    private Double latitude;
    private Double longitude;
    private LocalDateTime locationTimestamp;
    private String lastSeen;
    private boolean isStolen;
}
