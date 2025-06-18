package com.antiTheftTracker.antiTheftTrackerApp.services.mailAndSmsServ;

import com.antiTheftTracker.antiTheftTrackerApp.utils.messaging.PhoneNumberUtil;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service

public class SmsServiceImpl implements SmsService{



    private final JavaMailSender mailSender;

    private final String defaultGateway;


    private final String email;

    @Autowired
    private PhoneNumberUtil phoneNumberUtil;


    public SmsServiceImpl(JavaMailSender mailSender, @Value("${sms.gateway}")String defaultGateway, @Value("${spring.mail.username}")String email, PhoneNumberUtil phoneUtil) {
        this.mailSender = mailSender;
        this.defaultGateway = defaultGateway;
        this.email = email;
        this.phoneNumberUtil = phoneUtil;
    }

    @Override
    public void sendSms(String phoneNumber, String message) throws MessagingException {
      String recipient = phoneNumberUtil.getEmailAddressForSms(phoneNumber, defaultGateway);
      if (recipient.isBlank()){
          throw new MessagingException("Invalid phone number");
        }
      var mimeMessage = mailSender.createMimeMessage();
      var helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      try{
          helper.setTo(recipient);
          helper.setFrom(email);
          helper.setText(message);
          helper.setSubject("Security Alert");
          mailSender.send(mimeMessage);
      }catch(MessagingException error){
          throw new RuntimeException("Failed to send SMS via email",error);
      }
    }
}
