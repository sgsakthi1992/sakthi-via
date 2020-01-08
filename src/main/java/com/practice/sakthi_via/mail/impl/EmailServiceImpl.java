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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class EmailServiceImpl implements EmailService {
    /**
     * JavaMailSender object.
     */
    private JavaMailSender javaMailSender;
    /**
     * Thymeleaf TemplateEngine Object.
     */
    private SpringTemplateEngine templateEngine;

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
    public void setTemplateEngine(
            final SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Implementation of Send mail method.
     *
     * @param to       To address
     * @param subject  From address
     * @param employee Employee details
     */
    @Override
    public void sendMail(final String to, final String subject,
                         final Employee employee) {
        String body = build("mailTemplate", employee);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);
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
