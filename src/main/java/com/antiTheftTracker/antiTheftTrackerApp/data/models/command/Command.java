package com.antiTheftTracker.antiTheftTrackerApp.data.models.command;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String type;
    private LocalDateTime issuedAt;
    private boolean executed = false;
    @ManyToOne
    private DeviceEntity device;
    private String simSerial;
    private Double latitude;
    private Double longitude;

}