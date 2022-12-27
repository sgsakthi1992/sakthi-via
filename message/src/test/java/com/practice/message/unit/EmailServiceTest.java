package com.practice.message.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.practice.message.factory.impl.MessagingFactoryImpl;
import com.practice.message.model.Content;
import java.util.Arrays;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

  @Mock
  JavaMailSender javaMailSender;

  @Mock
  ITemplateEngine templateEngine;

  @InjectMocks
  MessagingFactoryImpl messagingFactory;

  @Test
  void send() throws MessagingException {
    //GIVEN
    ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
    Content content = Content.builder()
        .setTo("employee@gmail.com,employee1@gmail.com")
        .setSubject("EMAIL_SUBJECT")
        .setBody(Map.of("name", "employee"))
        .setTemplate("MAIL_TEMPLATE").createMail();

    when(templateEngine.process(eq("MAIL_TEMPLATE"), any(Context.class))).thenReturn("content");
    when(javaMailSender.createMimeMessage()).thenReturn(new JavaMailSenderImpl().createMimeMessage());

    //WHEN
    messagingFactory.create("email").send(content);
    //THEN
    verify(javaMailSender).send(captor.capture());
    assertEquals("EMAIL_SUBJECT", captor.getValue().getSubject());
    assertEquals(2, captor.getValue().getAllRecipients().length);
    assertTrue(Arrays.stream(captor.getValue().getAllRecipients())
        .allMatch(to -> "employee@gmail.com,employee1@gmail.com".contains(to.toString())));
  }

}
