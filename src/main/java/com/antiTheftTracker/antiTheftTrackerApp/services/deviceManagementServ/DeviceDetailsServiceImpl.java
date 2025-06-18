package com.antiTheftTracker.antiTheftTrackerApp.services.deviceManagementServ;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceDetail;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceDetailsRepository;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceDetailsResponse;
import com.antiTheftTracker.antiTheftTrackerApp.utils.androidManagement.AndroidManagementFactory;
import com.antiTheftTracker.antiTheftTrackerApp.utils.mapper.DeviceDetailsMapper;
import com.antiTheftTracker.antiTheftTrackerApp.utils.mapper.DeviceEntityMapper;
import com.google.api.services.androidmanagement.v1.model.Device;
import com.google.api.services.androidmanagement.v1.model.TelephonyInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DeviceDetailsServiceImpl implements DeviceDetailsService {
    private final AndroidManagementFactory androidManagementFactory;
    private final DeviceDetailsRepository deviceDetailsRepository;
    private final DeviceEntityRepository deviceEntityRepository;

    @Override
    public DeviceDetailsResponse getDeviceDetails(String deviceId) throws IOException {
        var device = fetchDeviceFromApi(deviceId);

        DeviceEntity deviceEntity = DeviceEntityMapper.toEntity(device);
        deviceEntityRepository.save(deviceEntity);

        DeviceDetail deviceDetail = mapToDeviceDetails(device, deviceEntity);
        deviceDetailsRepository.save(deviceDetail);


        return DeviceDetailsMapper.buildReturnResponse(deviceDetail);
    }


    private DeviceDetail mapToDeviceDetails(Device device, DeviceEntity deviceEntity) {
        var deviceDetail = new DeviceDetail();
        deviceDetail.setId(extractDeviceId(device));
        deviceDetail.setManufacturer(extractManufacturer(device));
        deviceDetail.setSerialNumber(extractSerialNumber(device));
        deviceDetail.setImei(extractImei(device));
        deviceDetail.setSimIccidSlot0(extractSimIccidSlot0(device));
        deviceDetail.setSimIccidSlot1(extractSimIccidSlot1(device));
        deviceDetail.setCarrierNameSlot0(extractCarrierNameSlot0(device));
        deviceDetail.setCarrierNameSlot1(extractCarrierNameSlot1(device));
        deviceDetail.setPhoneNumberSlot0(extractPhoneNumberSlot0(device));
        deviceDetail.setPhoneNumberSlot1(extractPhoneNumberSlot1(device));
        deviceDetail.setDevice(deviceEntity);
        return deviceDetail;
    }

    private Device fetchDeviceFromApi(String deviceId) throws IOException {
        var client = androidManagementFactory.getClient();
        return client.enterprises().devices().get(deviceId).execute();
    }

    private String extractDeviceId(Device device) {
        return device.getName();
    }

    private String extractManufacturer(Device device) {
        return Optional.ofNullable(device.getHardwareInfo())
                .map(hardwareInfo -> hardwareInfo.getManufacturer())
                .orElse(null);
    }

    private String extractSerialNumber(Device device) {
        return Optional.ofNullable(device.getHardwareInfo())
                .map(hardwareInfo -> hardwareInfo.getSerialNumber())
                .orElse(null);
    }

    private String extractImei(Device device) {
        return Optional.ofNullable(device.getNetworkInfo())
                .map(networkInfo -> networkInfo.getImei())
                .orElse(null);
    }

    private String extractSimIccidSlot0(Device device) {
        List<TelephonyInfo> telephonyInfos = device.getNetworkInfo().getTelephonyInfos();
        return (telephonyInfos != null && !telephonyInfos.isEmpty()) ? telephonyInfos.get(0).getIccId() : null;
    }

    private String extractSimIccidSlot1(Device device) {
        List<TelephonyInfo> telephonyInfos = device.getNetworkInfo().getTelephonyInfos();
        return (telephonyInfos != null && telephonyInfos.size() > 1) ? telephonyInfos.get(1).getIccId() : null;
    }

    private String extractCarrierNameSlot0(Device device) {
        List<TelephonyInfo> telephonyInfos = device.getNetworkInfo().getTelephonyInfos();
        return (telephonyInfos != null && !telephonyInfos.isEmpty()) ? telephonyInfos.get(0).getCarrierName() : null;
    }

    private String extractCarrierNameSlot1(Device device) {
        List<TelephonyInfo> telephonyInfos = device.getNetworkInfo().getTelephonyInfos();
        return (telephonyInfos != null && telephonyInfos.size() > 1) ? telephonyInfos.get(1).getCarrierName() : null;
    }

    private String extractPhoneNumberSlot0(Device device) {
        List<TelephonyInfo> telephonyInfos = device.getNetworkInfo().getTelephonyInfos();
        return (telephonyInfos != null && !telephonyInfos.isEmpty()) ? telephonyInfos.get(0).getPhoneNumber() : null;
    }

    private String extractPhoneNumberSlot1(Device device) {
        List<TelephonyInfo> telephonyInfos = device.getNetworkInfo().getTelephonyInfos();
        return (telephonyInfos != null && telephonyInfos.size() > 1) ? telephonyInfos.get(1).getPhoneNumber() : null;
    }




}
