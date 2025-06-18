package com.antiTheftTracker.antiTheftTrackerApp.dtos.request.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommandRequest {
    private String type;
    private String deviceId;

}
