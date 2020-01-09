/*
 * Copyright (c) 2020.
 */
/**
 * Email Service Implementation.
 *
 * @author Sakthi_Subramaniam
 */
package com.practice.sakthi_via.mail.impl;

import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
public class EmailServiceImpl implements EmailService {
    /**
     * JavaMailSender object.
     */
    private JavaMailSender javaMailSender;
    /**
     * Thymeleaf TemplateEngine Object.
     */
    private TemplateEngine templateEngine;

    /**
     * Setter for JavaMailSender object.
     *
     * @param javaMailSender JavaMailSender object
     */
    @Autowired
    public void setJavaMailSender(
            final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Setter for TemplateEngine object.
     *
     * @param templateEngine TemplateEngine object
     */
    @Autowired
    public void setTemplateEngine(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Implementation of Send mail method.
     *
     * @param to       To address
     * @param subject  From address
     * @param employee Employee details
     * @throws MessagingException exception
     */
    @Override
    public void sendMail(final String to, final String subject,
                         final Employee employee) throws MessagingException {
        String body = build("mailTemplate", employee);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body, true);
        javaMailSender.send(mimeMessage);
    }

    private String build(final String template,
                         final Employee employee) {
        Context context = new Context();
        context.setVariable("id", employee.getId());
        context.setVariable("name", employee.getName());
        context.setVariable("username", employee.getUsername());
        context.setVariable("age", employee.getAge());
        context.setVariable("email", employee.getEmail());
        return templateEngine.process(template, context);
    }
}
