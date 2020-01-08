package com.practice.sakthi_via.mail;

@FunctionalInterface
public interface EmailService {
    /**
     * Send mail abstract method.
     *
     * @param to      To address
     * @param subject From address
     * @param body    Mail body
     */
    void sendMail(String to, String subject, String body);
}
