package com.practice.sakthi_via.mail;

import com.practice.sakthi_via.model.Employee;

@FunctionalInterface
public interface EmailService {
    /**
     * Send mail abstract method.
     *
     * @param to       To address
     * @param subject  From address
     * @param employee Employee details
     */
    void sendMail(String to, String subject, Employee employee);
}
