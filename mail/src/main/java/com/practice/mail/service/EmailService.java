package com.practice.mail.service;

import com.practice.mail.model.Mail;

import javax.mail.MessagingException;

@FunctionalInterface
public interface EmailService {
    /**
     * Send mail abstract method.
     *
     * @param mail Mail model object
     * @throws MessagingException exception
     */
    void sendMail(Mail mail) throws MessagingException;
}
