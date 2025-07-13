package com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String recipient, String subject, String message) {
        System.out.println("EmailServiceImpl.sendEmail called with:");
        System.out.println("  Recipient: " + recipient);
        System.out.println("  Subject: " + subject);
        System.out.println("  Message length: " + message.length());
        
        try {
            var mimeMessage = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("embracegod92@gmail.com"); // Set the from address
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(message, true);

            System.out.println("About to send email via mailSender...");
            mailSender.send(mimeMessage);
            System.out.println("Email sent successfully to: " + recipient);
        } catch (MessagingException error) {
            System.err.println("Failed to send email to " + recipient + ": " + error.getMessage());
            System.err.println("Full error: " + error);
            throw new RuntimeException("Failed to send email", error);
        } catch (Exception error) {
            System.err.println("Unexpected error sending email: " + error.getMessage());
            error.printStackTrace();
            throw new RuntimeException("Failed to send email", error);
        }
    }
}
