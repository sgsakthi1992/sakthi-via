package com.practice.message.service;

import com.practice.message.model.Content;
import org.springframework.stereotype.Service;

@FunctionalInterface
@Service
public interface MessagingService {
    /**
     * Send method.
     *
     * @param content Message content
     */
    void send(Content content);
}
