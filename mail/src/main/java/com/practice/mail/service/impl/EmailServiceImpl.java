package com.practice.mail.service.impl;

import com.practice.mail.model.Mail;
import com.practice.mail.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
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
    public EmailServiceImpl(final JavaMailSender javaMailSender,
                            final ITemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
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
        mimeMessageHelper.setTo(InternetAddress.parse(mail.getTo()));
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
