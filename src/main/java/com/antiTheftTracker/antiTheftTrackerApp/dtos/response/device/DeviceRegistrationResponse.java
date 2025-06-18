package com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeviceRegistrationResponse {
    private String tokenId;
    private String tokenValue;
    private String enrollmentUrl;
    private String qrCodeImageUrl;
}
