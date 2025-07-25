package com.antiTheftTracker.antiTheftTrackerApp.controllers.device;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device.DeviceLocationRequest;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceLocationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/location/update")
    public ResponseEntity<Void> updateDeviceLocation(@RequestBody DeviceLocationRequest request) {
        locationService.updateDeviceLocation(request.getDeviceId(), request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/location_/{deviceId}")
    public ResponseEntity<?> getDeviceLocation(@PathVariable String deviceId) {
        var location = locationService.getDeviceLocation(deviceId);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(location);
    }


}
