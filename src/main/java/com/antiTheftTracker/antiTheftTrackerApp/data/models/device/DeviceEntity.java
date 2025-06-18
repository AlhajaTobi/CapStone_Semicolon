package com.antiTheftTracker.antiTheftTrackerApp.data.models.device;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="devices")
@Getter
@Setter
public class DeviceEntity {

    @Id
    private String id;

    private String policyName;
    private LocalDateTime enrollmentTime = LocalDateTime.now();
    private boolean isStolen = false;
    private String lastKnownSim;
    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
    private String lastSeen;
    private String fcmToken;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceDetail> deviceDetail;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrustedContact> trustedContacts;

    @OneToOne(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private DeviceLocation currentLocation;
}
