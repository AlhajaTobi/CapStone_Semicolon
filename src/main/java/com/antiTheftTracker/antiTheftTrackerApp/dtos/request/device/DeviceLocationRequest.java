package com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeviceLocationRequest {
    private String deviceId;
    private double latitude;
    private double longitude;

}
