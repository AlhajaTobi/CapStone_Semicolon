package com.antiTheftTracker.antiTheftTrackerApp.utils.mapper;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceDetail;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.device.DeviceDetailsResponse;
import org.springframework.stereotype.Component;

@Component
public class DeviceDetailsMapper {

    public static DeviceDetailsResponse buildReturnResponse(DeviceDetail deviceDetail) {
        return new DeviceDetailsResponse(
                deviceDetail.getId(),
                deviceDetail.getManufacturer(),
                deviceDetail.getSerialNumber(),
                deviceDetail.getImei(),
                deviceDetail.getSimIccidSlot0(),
                deviceDetail.getSimIccidSlot1(),
                deviceDetail.getCarrierNameSlot1(),
                deviceDetail.getCarrierNameSlot0(),
                deviceDetail.getPhoneNumberSlot0(),
                deviceDetail.getPhoneNumberSlot1()


        );
    }
}
