package com.practice.message.unit;

import com.practice.message.factory.impl.MessagingFactoryImpl;
import com.practice.message.model.Content;
import com.practice.message.service.impl.SmsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.ITemplateEngine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    ITemplateEngine templateEngine;

    @InjectMocks
    MessagingFactoryImpl messagingFactory;

    @Captor
    ArgumentCaptor<String> captor;

    @Test
    void send() {
        //GIVEN
        Map<String, Object> body = new HashMap<>();
        body.put("otp", 1234567);
        body.put("startTime", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        body.put("expiryTime", LocalDateTime.now().plus(5, ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        Content content = Content.builder()
                .setTo("+14108675310")
                .setBody(body).createMail();
        SmsService smsService = (SmsService) messagingFactory.create("sms");
        ReflectionTestUtils.setField(smsService, "authToken",
                "ef3029816543c3f6d196849957335e9a");
        ReflectionTestUtils.setField(smsService, "accountSid",
                "AC4f8e0922ba16b38a38f5068d04c66728");
        ReflectionTestUtils.setField(smsService, "twiloPhoneNumber",
                "+15005550006");

        //WHEN
        //THEN
        Assertions.assertDoesNotThrow(() -> smsService.send(content));
    }
}
