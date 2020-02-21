package com.practice.message.model;

import java.util.Map;

public final class Content {
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
    private final Map<String, Object> body;
    /**
     * Mail template.
     */
    private final String template;

    /**
     * Mail parameterized constructor.
     *
     * @param builder Builder object
     */
    private Content(final MailBuilder builder) {
        this.to = builder.to;
        this.subject = builder.subject;
        this.body = builder.body;
        this.template = builder.template;
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
    public Map<String, Object> getBody() {
        return body;
    }

    /**
     * Getter for mail template.
     *
     * @return mail template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * MailBuilder object.
     *
     * @return mail builder object
     */
    public static MailBuilder builder() {
        return new MailBuilder();
    }

    public static final class MailBuilder {
        /**
         * To email id.
         */
        private String to;
        /**
         * Mail subject.
         */
        private String subject;
        /**
         * Mail content.
         */
        private Map<String, Object> body;
        /**
         * Mail template.
         */
        private String template;

        /**
         * Set to address.
         *
         * @param to email address
         * @return to email address
         */
        public MailBuilder setTo(final String to) {
            this.to = to;
            return this;
        }

        /**
         * Set subject.
         *
         * @param subject subject
         * @return subject
         */
        public MailBuilder setSubject(final String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Set content.
         *
         * @param body content
         * @return content
         */
        public MailBuilder setBody(final Map<String, Object> body) {
            this.body = body;
            return this;
        }

        /**
         * Set template.
         *
         * @param template template
         * @return template
         */
        public MailBuilder setTemplate(final String template) {
            this.template = template;
            return this;
        }

        /**
         * Build mail.
         *
         * @return mail object.
         */
        public Content createMail() {
            return new Content(this);
        }
    }
}
