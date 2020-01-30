package com.practice.message.factory.impl;

import com.practice.message.factory.AbstractFactory;
import com.practice.message.service.MessagingService;
import com.practice.message.service.impl.EmailService;
import com.practice.message.service.impl.SmsService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;

@Service
public class MessagingFactoryImpl implements AbstractFactory<MessagingService> {
    /**
     * JavaMailSender object.
     */
    private final JavaMailSender javaMailSender;
    /**
     * Thymeleaf TemplateEngine Object.
     */
    private final ITemplateEngine templateEngine;

    /**
     * Parameterized constructor.
     *
     * @param javaMailSender JavaMailSender object
     * @param templateEngine TemplateEngine object
     */
    MessagingFactoryImpl(final JavaMailSender javaMailSender,
                         final ITemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Overridden method to return the object.
     *
     * @param type email or sms
     * @return email or sms class objects
     */
    @Override
    public MessagingService create(final String type) {
        if (type.contentEquals("email")) {
            return new EmailService(javaMailSender, templateEngine);
        } else if (type.contentEquals("sms")) {
            return new SmsService();
        }
        return null;
    }
}
