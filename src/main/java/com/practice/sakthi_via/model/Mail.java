package com.practice.sakthi_via.model;

import java.util.Map;

public final class Mail {
    /**
     * To email id.
     */
    private final String to;
    /**
     * Mail subject.
     */
    private final String subject;
    /**
     * Mail content.
     */
    private final Map<String, Object> content;
    /**
     * Mail template.
     */
    private final String template;

    /**
     * Mail parameterized constructor.
     *
     * @param to       to email id
     * @param subject  mail subject
     * @param content  mail content
     * @param template mail template
     */
    public Mail(final String to, final String subject,
                final Map<String, Object> content, final String template) {
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.template = template;
    }

    /**
     * Getter for to.
     *
     * @return to email id
     */
    public String getTo() {
        return to;
    }

    /**
     * Getter for subject.
     *
     * @return mail subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Getter for mail content.
     *
     * @return mail content
     */
    public Map<String, Object> getContent() {
        return content;
    }

    /**
     * Getter for mail template.
     *
     * @return mail template
     */
    public String getTemplate() {
        return template;
    }
}
