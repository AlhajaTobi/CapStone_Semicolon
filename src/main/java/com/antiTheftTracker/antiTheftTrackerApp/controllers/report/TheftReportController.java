package com.antiTheftTracker.antiTheftTrackerApp.controllers.report;

import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.report.TheftReportRequest;
import com.antiTheftTracker.antiTheftTrackerApp.services.TheftReportManagement.TheftReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/theft")
@RequiredArgsConstructor
public class TheftReportController {

    private final TheftReportService theftReportService;

    @PostMapping("/report")
    ResponseEntity<Void> reportTheft(@RequestBody TheftReportRequest request) {
        theftReportService.handleTheftReport(request);
        return ResponseEntity.ok().build();

    }
}
