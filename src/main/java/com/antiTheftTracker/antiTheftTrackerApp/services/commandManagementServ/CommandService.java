package com.antiTheftTracker.antiTheftTrackerApp.services.commandManagementServ;


public interface CommandService {
    void issueLockCommand(String deviceId);
    void issueWipeCommand(String deviceId);

    }
