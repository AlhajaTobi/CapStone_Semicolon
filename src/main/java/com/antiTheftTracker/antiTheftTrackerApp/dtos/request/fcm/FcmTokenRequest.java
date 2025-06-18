package com.antiTheftTracker.antiTheftTrackerApp.dtos.request.fcm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmTokenRequest {
    private String deviceId;
    private String fcmToken;
}
