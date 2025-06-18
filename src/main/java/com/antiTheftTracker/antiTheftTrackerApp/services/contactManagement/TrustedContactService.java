package com.antiTheftTracker.antiTheftTrackerApp.services.contactManagement;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.contact.TrustedContactRequest;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.contact.TrustedContactResponse;

import java.util.List;

public interface TrustedContactService {

    List<TrustedContact> getContactsForDevice(String id);

    TrustedContactResponse addTrustedContact(String deviceId, TrustedContactRequest request);

    TrustedContactResponse deleteTrustedContact(String deviceId, String contactId);

    TrustedContactResponse editTrustedContact(String deviceId, String contactId, TrustedContactRequest request);
}