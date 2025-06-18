package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceDetail;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceDetailsRepository;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.device.DeviceRegistrationRequest;
import com.antiTheftTracker.antiTheftTrackerApp.exceptions.DeviceIdAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@Transactional
public class DeviceMetaDataRegImpl implements DeviceMetadataRegistration{

    private final DeviceDetailsRepository deviceDetailsRepository;

    private final DeviceEntityRepository deviceRepository;
    @Override
    public void registerDeviceMetadata(String deviceId, DeviceRegistrationRequest request) {
        try {
            if (deviceRepository.existsById(deviceId)) {
                throw new DeviceIdAlreadyExistsException("Device with ID " + deviceId + " already exists. " + HttpStatus.CONFLICT);
            }

            DeviceEntity newDevice = new DeviceEntity();
            newDevice.setId(deviceId);
            newDevice.setStolen(false);
            newDevice.setFcmToken(request.getFcmToken());
            newDevice.setLatitude(request.getLatitude());
            newDevice.setLongitude(request.getLongitude());
            newDevice.setDeviceDetail(new ArrayList<>());

            DeviceDetail detail = new DeviceDetail();
            detail.setId(UUID.randomUUID().toString());
            detail.setManufacturer(request.getManufacturer());
            detail.setDeviceModel(request.getDeviceModel());
            detail.setSerialNumber(request.getSerialNumber());
            detail.setImei(request.getImei());
            detail.setSimIccidSlot0(request.getSimSerialNumber());
            detail.setPhoneNumberSlot0(request.getPhoneNumber());
            detail.setCarrierNameSlot0(request.getCarrierName());
            detail.setLatitude(request.getLatitude());
            detail.setLongitude(request.getLongitude());
            detail.setDevice(newDevice);

            newDevice.getDeviceDetail().add(detail);

            deviceRepository.save(newDevice);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void updateImei(String deviceId, String imei) {
        DeviceEntity device = deviceRepository.findById(deviceId).orElseThrow();
        DeviceDetail detail = device.getDeviceDetail().stream().findFirst().orElse(new DeviceDetail());

        detail.setImei(imei);
        device.setDeviceDetail(List.of(detail));
        deviceDetailsRepository.save(detail);
    }

    @Override
    public void updatePhoneNumber(String deviceId, String phoneNumber) {
        DeviceEntity device = deviceRepository.findById(deviceId).orElseThrow();
        DeviceDetail detail = device.getDeviceDetail().stream().findFirst().orElse(new DeviceDetail());

        detail.setPhoneNumberSlot0(phoneNumber);
        device.setDeviceDetail(List.of(detail));
        deviceDetailsRepository.save(detail);
    }


}
