package com.antiTheftTracker.antiTheftTrackerApp.data.repositories.command;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.command.Command;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRepository extends JpaRepository<Command, String> {
}
