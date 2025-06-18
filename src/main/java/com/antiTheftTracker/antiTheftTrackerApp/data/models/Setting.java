package com.antiTheftTracker.antiTheftTrackerApp.data.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="settings")
public class Setting {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String key;

        private String value;


}
