package com.practice.mail.unit;

import com.practice.mail.model.Mail;
import com.practice.mail.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class EmailServiceImplTest {

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    ITemplateEngine templateEngine;

    @InjectMocks
    EmailServiceImpl emailService;

    @Test
    void sendMail() throws MessagingException {
        //GIVEN
        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        Mail.MailBuilder builder = Mail.builder();
        builder.setTo("employee@gmail.com,employee1@gmail.com");
        builder.setSubject("EMAIL_SUBJECT");
        builder.setContent(Map.of("name", "employee"));
        builder.setTemplate("MAIL_TEMPLATE");

        when(templateEngine.process(eq("MAIL_TEMPLATE"), any(Context.class))).thenReturn("content");
        when(javaMailSender.createMimeMessage()).thenReturn(new JavaMailSenderImpl().createMimeMessage());

        //WHEN
        emailService.sendMail(builder.createMail());
        //THEN
        verify(javaMailSender).send(captor.capture());
        assertEquals("EMAIL_SUBJECT", captor.getValue().getSubject());
        assertEquals(2, captor.getValue().getAllRecipients().length);
        assertTrue(Arrays.stream(captor.getValue().getAllRecipients())
                .allMatch(to -> "employee@gmail.com,employee1@gmail.com" .contains(to.toString())));
    }
}