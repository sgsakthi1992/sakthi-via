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
    private final Map content;

    /**
     * Mail parameterized constructor.
     *
     * @param to      to email id
     * @param subject mail subject
     * @param content mail content
     */
    public Mail(final String to, final String subject, final Map content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
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
    public Map getContent() {
        return content;
    }
}
