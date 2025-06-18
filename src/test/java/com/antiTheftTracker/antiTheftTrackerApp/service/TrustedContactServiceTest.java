package com.antiTheftTracker.antiTheftTrackerApp.service;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.contact.TrustedContactRepository;
import com.antiTheftTracker.antiTheftTrackerApp.data.repositories.device.DeviceEntityRepository;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.request.contact.TrustedContactRequest;
import com.antiTheftTracker.antiTheftTrackerApp.dtos.response.contact.TrustedContactResponse;
import com.antiTheftTracker.antiTheftTrackerApp.exceptions.DeviceNotFoundException;
import com.antiTheftTracker.antiTheftTrackerApp.exceptions.IllegalTrustedContactException;
import com.antiTheftTracker.antiTheftTrackerApp.services.contactManagement.TrustedContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrustedContactServiceTest {

    @Autowired
    TrustedContactService trustedContactService;

    @Autowired
    DeviceEntityRepository deviceEntityRepository;

    @Autowired
    TrustedContactRepository trustedContactRepository;

    DeviceEntity testDevice;

    @BeforeEach
    void setUp() {
        deviceEntityRepository.deleteAll();
        trustedContactRepository.deleteAll();

            testDevice = new DeviceEntity();
            testDevice.setId("Test Device");
            testDevice = deviceEntityRepository.save(testDevice);

    }

    private static TrustedContactRequest validRequest(String name, String phoneNumber, String email) {
        TrustedContactRequest request = new TrustedContactRequest();
        request.setName(name);
        request.setPhoneNumber(phoneNumber);
        request.setEmail(email);
        request.setSmsEnabled(true);
        request.setEmailEnabled(true);
        return request;
    }

    @Test
    void testThatContactsAreAdded() {

        TrustedContactRequest request = validRequest("Adewale", "09012345234", "adewale@maleek.com");
        TrustedContactResponse response = trustedContactService.addTrustedContact(testDevice.getId(), request);
        assertEquals("Successfully added contact", response.getMessage());

    }


    @Test
    void testAddContactFailsWhenDeviceNotFound() {
        String invalidDeviceId = "non-existent-id";
        TrustedContactRequest request = validRequest("Bola", "bola@example.com", "08011111111");

        DeviceNotFoundException ex = assertThrows(DeviceNotFoundException.class, () ->
                trustedContactService.addTrustedContact(invalidDeviceId, request)
        );
        assertTrue(ex.getMessage().contains("Device not found"));
    }

    @Test
    void testAddContactFailsWithMissingFields() {
        TrustedContactRequest request = new TrustedContactRequest();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                trustedContactService.addTrustedContact(testDevice.getId(), request)
        );
        assertTrue(ex.getMessage().equals("All fields (name, phone number, email) are required"));
    }

    @Test
    void testAddContactFailsOnDuplicate() {
        TrustedContactRequest request = validRequest("Tola", "duplicate@example.com", "08022222222");

        trustedContactService.addTrustedContact(testDevice.getId(), request);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                trustedContactService.addTrustedContact(testDevice.getId(), request)
        );
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void testAddContactFailsWhenMoreThan3() {

        trustedContactService.addTrustedContact(testDevice.getId(), validRequest("One", "1@mail.com", "0801"));
        trustedContactService.addTrustedContact(testDevice.getId(), validRequest("Two", "2@mail.com", "0802"));
        trustedContactService.addTrustedContact(testDevice.getId(), validRequest("Three", "3@mail.com", "0803"));
        System.out.println(trustedContactRepository.findAll());

        IllegalTrustedContactException ex = assertThrows(IllegalTrustedContactException.class, () ->
                trustedContactService.addTrustedContact(testDevice.getId(), validRequest("Four", "4@mail.com", "0804"))
        );
        assertTrue(ex.getMessage().contains("can't add more than 3"));
    }

    @Test
    void testDeleteContactSuccessfully() {
        TrustedContactRequest request = validRequest("Wale", "wale@example.com", "08000000000");
        trustedContactService.addTrustedContact(testDevice.getId(), request);

        TrustedContact contact = trustedContactRepository.findAll().get(0);

        TrustedContactResponse response = trustedContactService.deleteTrustedContact(testDevice.getId(),contact.getId());

        assertEquals("Successfully deleted contact", response.getMessage());
        assertTrue(trustedContactRepository.findById(contact.getId()).isEmpty());
    }

    @Test
    void testDeleteContactFailsWithInvalidId() {
        IllegalTrustedContactException ex = assertThrows(IllegalTrustedContactException.class, () ->
                trustedContactService.deleteTrustedContact("device-123","nonexistent-id")
        );
        assertTrue(ex.getMessage().contains("can't delete"));
    }

}
