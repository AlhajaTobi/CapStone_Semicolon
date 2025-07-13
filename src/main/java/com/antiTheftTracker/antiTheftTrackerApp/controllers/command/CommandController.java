package com.antiTheftTracker.antiTheftTrackerApp.controllers.command;

import com.antiTheftTracker.antiTheftTrackerApp.services.commandManagementServ.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/commands")
@RequiredArgsConstructor
public class CommandController {
    private final CommandService commandService;


    @PostMapping("/lock/{deviceId}")
    public ResponseEntity<Void> lockDevice(@PathVariable String deviceId) {
        commandService.issueLockCommand(deviceId);
        return ResponseEntity.accepted().build();
    }


    @PostMapping("/wipe/{deviceId}")
    public ResponseEntity<Void> wipeDevice(@PathVariable String deviceId) {
        commandService.issueWipeCommand(deviceId);
        return ResponseEntity.accepted().build();
    }
}
