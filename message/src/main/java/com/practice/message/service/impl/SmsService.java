/*
 * Copyright (c) 2020.
 */

package com.practice.message.service.impl;

import com.practice.message.model.Content;
import com.practice.message.service.MessagingService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsService implements MessagingService {
    /**
     * ACCOUNT_SID.
     */
    private static final String ACCOUNT_SID =
            "AC3708314765ca28d112fa81ee56492103";
    /**
     * AUTH_TOKEN.
     */
    private static final String AUTH_TOKEN = "931b13a6891249783d03bf5c96c9650c";
    /**
     * TWILIO_NUMBER.
     */
    private static final String TWILIO_NUMBER = "+15416124108";

    /**
     * Overridden method to implement text message service.
     *
     * @param content text message content
     */
    public void send(final Content content) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber(content.getTo()),
                new PhoneNumber(TWILIO_NUMBER),
                content.getBody().get("otp").toString())
                .create();
    }
}
