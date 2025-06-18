package com.antiTheftTracker.antiTheftTrackerApp.service;

import com.antiTheftTracker.antiTheftTrackerApp.data.models.device.DeviceEntity;
import com.antiTheftTracker.antiTheftTrackerApp.data.models.contact.TrustedContact;
import com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ.EmailService;
import com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ.SmsService;
import com.antiTheftTracker.antiTheftTrackerApp.services.notificationsManagement.NotificationServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

        @Mock
        private SmsService smsService;
        @Mock
        private EmailService emailService;
        @InjectMocks
        private NotificationServiceImpl notificationService;

        @Test
        void testSendTheftAlert_sendsBothSmsAndEmail_whenEnabled() throws MessagingException {
            var contact = mock(TrustedContact.class);
            var device = mock(DeviceEntity.class);

            when(contact.isSmsEnabled()).thenReturn(true);
            when(contact.isEmailEnabled()).thenReturn(true);
            when(contact.getPhoneNumber()).thenReturn("08012345678");
            when(contact.getEmail()).thenReturn("user@example.com");

            when(device.getId()).thenReturn("device-123");
            when(device.getCurrentLocation().getLatitude()).thenReturn(6.456);
            when(device.getCurrentLocation().getLongitude()).thenReturn(3.876);

            notificationService.sendTheftAlert(contact, device);

            verify(smsService).sendSms(eq("08012345678"), anyString());
            verify(emailService).sendEmail(eq("user@example.com"), anyString(), anyString());
        }

        @Test
        void testSendTheftAlert_skipsWhenDisabled() throws MessagingException {
            var contact = mock(TrustedContact.class);
            var device = mock(DeviceEntity.class);

            lenient().when(contact.isSmsEnabled()).thenReturn(false);
            lenient().when(contact.isEmailEnabled()).thenReturn(false);

            notificationService.sendTheftAlert(contact, device);

            verifyNoInteractions(smsService, emailService);
        }

}
