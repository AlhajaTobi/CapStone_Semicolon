package com.antiTheftTracker.antiTheftTrackerApp.controllers.contact;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.contact.TrustedContactRequest;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.contact.TrustedContactResponse;
import com.antiTheftTracker.antiTheftTrackerApp.services.contactManagement.TrustedContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/trusted-contact")
@RequiredArgsConstructor
class TrustedContactsController {

    private final TrustedContactService trustedContactService;

    @PostMapping("/{deviceId}/contacts")
    public ResponseEntity<TrustedContactResponse> addTrustedContact(@PathVariable String deviceId, @Valid @RequestBody TrustedContactRequest trustedContact) {
        trustedContactService.addTrustedContact(deviceId, trustedContact);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<List<TrustedContact>> getTrustedContacts(@PathVariable String deviceId) {
        return ResponseEntity.ok(trustedContactService.getContactsForDevice(deviceId));
    }

    @DeleteMapping("/{deviceId}/{contactId}")
    public ResponseEntity<TrustedContactResponse> deleteTrustedContact(@PathVariable String deviceId, @PathVariable String contactId) {
        trustedContactService.deleteTrustedContact(deviceId, contactId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{deviceId}/{contactId}")
    public ResponseEntity<TrustedContactResponse> editTrustedContact(
            @PathVariable String deviceId,
            @PathVariable String contactId,
            @Valid @RequestBody TrustedContactRequest request
    ) {
        TrustedContactResponse response = trustedContactService.editTrustedContact(deviceId, contactId, request);
        return ResponseEntity.ok(response);
    }
}
