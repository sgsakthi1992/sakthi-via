package com.practice.sakthi_via.mail;

import com.practice.sakthi_via.model.Mail;

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
