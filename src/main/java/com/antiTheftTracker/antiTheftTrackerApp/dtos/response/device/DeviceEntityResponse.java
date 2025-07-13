package com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class DeviceEntityResponse {
    private String deviceId;
    private String policyName;
    private String enrollmentTime;
    private boolean isStolen;
    private String lastSeen;
    private Double latitude;
    private Double longitude;
    private LocalDateTime locationTimestamp;
    private String deviceModel;
    private String imei;
}
