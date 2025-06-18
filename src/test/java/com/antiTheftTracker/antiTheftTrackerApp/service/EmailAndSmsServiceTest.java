package com.antiTheftTracker.antiTheftTrackerApp.service;
import com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ.EmailServiceImpl;
import com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ.SmsServiceImpl;
import com.antiTheftTracker.antiTheftTrackerApp.utils.messaging.PhoneNumberUtil;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class EmailAndSmsServiceTest {

        @Mock
        private JavaMailSender mailSender;

        @InjectMocks
        private EmailServiceImpl emailService;

        @InjectMocks
        private SmsServiceImpl smsService;

        @Mock
        private PhoneNumberUtil phoneNumberUtil;

        @Mock
        private MimeMailMessage mimeMessage;

        @Captor
        private ArgumentCaptor<MimeMailMessage> mimeCaptor;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage.getMimeMessage());
        }


        @Test
        void shouldSendEmailSuccessfully() {
            assertDoesNotThrow(() -> {
                emailService.sendEmail("test@example.com", "Test", "This is a test");
            });
            verify(mailSender, times(1)).send((MimeMessagePreparator) mimeMessage);
        }

        @Test
        void shouldFailSendingEmailDueToMessagingException() {
            doThrow(new RuntimeException("SMTP error")).when(mailSender).send((MimeMessagePreparator) mimeMessage);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> {
                emailService.sendEmail("bad@example.com", "Fail", "Message");
            });

            assertTrue(ex.getMessage().contains("Failed to send email"));
        }


        @Test
        void shouldSendMockSmsSuccessfully() throws Exception {
            smsService = new SmsServiceImpl(mailSender, "mock", "you@example.com", phoneNumberUtil);
            when(phoneNumberUtil.getEmailAddressForSms("08012345678", "mock"))
                    .thenReturn("test+2348012345678@mock.local");

            assertDoesNotThrow(() -> {
                smsService.sendSms("08012345678", "Mock SMS message");
            });
            verify(mailSender).send((MimeMessagePreparator) mimeMessage);
        }

        @Test
        void shouldThrowWhenPhoneNumberInvalid() {
            smsService = new SmsServiceImpl(mailSender, "mock", "you@example.com", phoneNumberUtil);
            when(phoneNumberUtil.getEmailAddressForSms(" ", "mock"))
                    .thenReturn("");

            MessagingException exception = assertThrows(MessagingException.class, () -> {
                smsService.sendSms(" ", "Message");
            });

            assertEquals("Invalid phone number", exception.getMessage());
        }

        @Test
        void shouldThrowIfCarrierGatewayIsNull() {
            smsService = new SmsServiceImpl(mailSender, "airtel", "you@example.com", phoneNumberUtil);
            when(phoneNumberUtil.getEmailAddressForSms("08011111111", "airtel"))
                    .thenThrow(new RuntimeException("No carrier gateway found"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                smsService.sendSms("08011111111", "Real SMS");
            });

            assertTrue(exception.getMessage().contains("Failed to send SMS via email"));
        }
    }


