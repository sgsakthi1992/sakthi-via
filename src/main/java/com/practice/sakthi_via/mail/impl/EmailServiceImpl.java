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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {
    /**
     * JavaMailSender object.
     */
    private JavaMailSender javaMailSender;

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
     * Implementation of Send mail method.
     *
     * @param to      To address
     * @param subject From address
     * @param body    Mail body
     */
    @Override
    public void sendMail(final String to,
                         final String subject, final String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);
    }
}
