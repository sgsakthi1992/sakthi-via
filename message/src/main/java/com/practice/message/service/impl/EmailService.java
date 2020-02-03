package com.practice.message.service.impl;

import com.practice.message.model.Content;
import com.practice.message.service.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class EmailService implements MessagingService {
    /**
     * JavaMailSender object.
     */
    private JavaMailSender javaMailSender;
    /**
     * Thymeleaf TemplateEngine Object.
     */
    private ITemplateEngine templateEngine;
    /**
     * Logger Object to log the details.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    /**
     * Parameterized constructor.
     *
     * @param javaMailSender JavaMailSender object
     * @param templateEngine TemplateEngine object
     */
    public EmailService(final JavaMailSender javaMailSender,
                        final ITemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Implementation of Send mail method.
     *
     * @param content Mail model object
     */
    @Override
    public void send(final Content content) {
        try {
            String body = build(content.getBody(), content.getTemplate());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            mimeMessageHelper.setTo(InternetAddress.parse(content.getTo()));
            mimeMessageHelper.setSubject(content.getSubject());
            mimeMessageHelper.setText(body, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Messaging exception in sendmail", e);
        }
    }

    private String build(final Map<String, Object> content,
                         final String template) {
        Context context = new Context();
        context.setVariables(content);
        return templateEngine.process(template, context);
    }
}
