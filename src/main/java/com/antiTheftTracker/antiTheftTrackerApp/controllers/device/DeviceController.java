package com.antiTheftTracker.antiTheftTrackerApp.controllers.device;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device.DeviceRegistrationRequest;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceDetailsResponse;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceEntityResponse;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceRegistrationResponse;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceDetailsService;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceEnrollmentService;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceEntityService;
import com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ.DeviceMetadataRegistration;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/device")
public class
DeviceController {

    private final DeviceEnrollmentService deviceEnrollmentService;
    private final DeviceEntityService deviceEntityService;
    private final DeviceDetailsService deviceDetailsService;
    private final DeviceMetadataRegistration deviceMetadataRegistration;

    @GetMapping("/enrol")
    public DeviceRegistrationResponse generateEnrollmentLink()throws IOException {
        return deviceEnrollmentService.generateEnrollmentLink();
    }

    @PostMapping("/register")
    public ResponseEntity<String> enrollDevice(@RequestBody DeviceRegistrationRequest request) {
        if (request.getDeviceId() == null || request.getDeviceId().isEmpty()) {
            return ResponseEntity.badRequest().body("Device ID is required");
        }

        deviceMetadataRegistration.registerDeviceMetadata(request.getDeviceId(), request);
        return ResponseEntity.ok("Device metadata registered successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<DeviceEntityResponse>> getAllDevices() {
        List<DeviceEntityResponse> devices = deviceEntityService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/meta-data")
    public ResponseEntity<List<DeviceEntityResponse>> getAllDeviceMetaData() {
        List<DeviceEntityResponse> devices = deviceEntityService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/meta-data/{deviceId}")
    public ResponseEntity<DeviceEntityResponse> getDeviceMetaData(@PathVariable String deviceId){
        DeviceEntityResponse response = deviceEntityService.getDeviceMetaData(deviceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details/{deviceId}")
    public ResponseEntity<DeviceDetailsResponse> getDeviceDetails(@PathVariable String deviceId)throws IOException{
       DeviceDetailsResponse response = deviceDetailsService.getDeviceDetails(deviceId);
       return ResponseEntity.ok(response);
    }

    @GetMapping("/location/{deviceId}")
    public ResponseEntity<Map<String, Object>> getDeviceLocation(@PathVariable String deviceId) {
        Map<String, Object> location = deviceEntityService.getDeviceLocation(deviceId);
        return ResponseEntity.ok(location);
    }

    @PostMapping("/location/report")
    public ResponseEntity<String> updateDeviceLocation(@RequestBody Map<String, Object> request) {
        String deviceId = (String) request.get("deviceId");
        Double latitude = (Double) request.get("latitude");
        Double longitude = (Double) request.get("longitude");
        
        deviceEntityService.updateDeviceLocation(deviceId, latitude, longitude);
        return ResponseEntity.ok("Location updated successfully");
    }

    @PostMapping("/imei")
    public ResponseEntity<String> updateImei(@RequestParam String deviceId, @RequestBody Map<String, String> payload) {
        deviceMetadataRegistration.updateImei(deviceId, payload.get("imei"));
        return ResponseEntity.ok("IMEI updated");
    }

    @PostMapping("/phone")
    public ResponseEntity<String> updatePhoneNumber(@RequestParam String deviceId, @RequestBody Map<String, String> payload) {
        deviceMetadataRegistration.updateImei(deviceId, payload.get("phoneNumber"));
        return ResponseEntity.ok("Phone number updated");
    }
}