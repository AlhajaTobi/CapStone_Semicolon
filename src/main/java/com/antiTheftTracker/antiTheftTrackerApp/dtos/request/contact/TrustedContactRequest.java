package com.antiTheftTracker.antiTheftTrackerApp.dtos.request.contact;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrustedContactRequest {

    private String deviceId;
    private String name;
    private String phoneNumber;
    private String email;
    private boolean isSmsEnabled;
    private boolean isEmailEnabled;
}
