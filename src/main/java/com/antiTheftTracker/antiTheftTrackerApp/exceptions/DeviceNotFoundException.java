package com.antiTheftTracker.antiTheftTrackerApp.exceptions;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(String message) {
        super(message);
    }
}
