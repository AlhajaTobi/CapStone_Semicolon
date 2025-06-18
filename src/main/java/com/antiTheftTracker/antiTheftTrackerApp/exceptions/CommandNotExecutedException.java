package com.antiTheftTracker.antiTheftTrackerApp.exceptions;

public class CommandNotExecutedException extends RuntimeException {
  public CommandNotExecutedException(String message) {
    super(message);
  }
}
