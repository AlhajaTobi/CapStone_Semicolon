package com.antiTheftTracker.antiTheftTrackerApp.controllers.device;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device.DeviceLocationRequest;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceLocationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/device/")
public class DeviceLocationController {

    @Autowired
    private DeviceLocationService locationService;

    @PostMapping("/location")
    public ResponseEntity<Void> updateDeviceLocation(@RequestBody DeviceLocationRequest request) {
        locationService.updateDeviceLocation(request.getDeviceId(), request);
        return ResponseEntity.accepted().build();
    }


}
