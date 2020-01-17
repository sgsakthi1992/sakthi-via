package com.practice.sakthi_via.mail.impl;

import com.practice.sakthi_via.mail.EmailService;
import com.practice.sakthi_via.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
     * @param mail Mail model object
     * @throws MessagingException exception
     */
    @Override
    public void sendMail(final Mail mail) throws MessagingException {
        String body = build(mail.getContent(), mail.getTemplate());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        mimeMessageHelper.setTo(mail.getTo());
        mimeMessageHelper.setSubject(mail.getSubject());
        mimeMessageHelper.setText(body, true);
        javaMailSender.send(mimeMessage);
    }

    private String build(final Map<String, Object> content,
                         final String template) {
        Context context = new Context();
        context.setVariables(content);
        return templateEngine.process(template, context);
    }
}
