package com.antiTheftTracker.antiTheftTrackerApp.exceptions;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
