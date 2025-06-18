package com.antiTheftTracker.antiTheftTrackerApp.data.models.device;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_locations")
@Getter
@Setter
public class DeviceLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;

    @OneToOne
    @JoinColumn(name = "device_id", nullable = false)
    private DeviceEntity device;

}
