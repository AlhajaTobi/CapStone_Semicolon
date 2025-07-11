package com.antiTheftTracker.antiTheftTrackerApp.data.models.contact;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="trusted_contacts")
public class TrustedContact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String phoneNumber;
    private String email;
    private boolean smsEnabled = true;
    private boolean emailEnabled = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    @JsonBackReference
    private DeviceEntity device;

}