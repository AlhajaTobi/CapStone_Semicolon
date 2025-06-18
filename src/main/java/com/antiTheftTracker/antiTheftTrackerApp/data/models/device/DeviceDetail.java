package com.antiTheftTracker.antiTheftTrackerApp.data.models.device;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "device_details")
@Getter
@Setter
public class DeviceDetail {
    @Id
    private String id;

    private String manufacturer;
    private String deviceModel;
    private String serialNumber;
    private String imei;
    private String simIccidSlot0;
    private String simIccidSlot1;
    private String carrierNameSlot0;
    private String carrierNameSlot1;
    private String phoneNumberSlot0;
    private String phoneNumberSlot1;
    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="device_id")
    private DeviceEntity device;


}
