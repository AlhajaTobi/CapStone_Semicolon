package com.antiTheftTracker.antiTheftTrackerApp.exceptions;

public class DeviceIdAlreadyExistsException extends RuntimeException {
    public DeviceIdAlreadyExistsException(String message) {
        super(message);
    }
}
