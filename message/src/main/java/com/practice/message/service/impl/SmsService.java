package com.practice.message.service.impl;

import com.practice.message.model.Content;
import com.practice.message.service.MessagingService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

public class SmsService implements MessagingService {

  /**
   * ACCOUNT_SID.
   */
  @Value("${via.sms.twilo.account.sid}")
  private String accountSid;
  /**
   * AUTH_TOKEN.
   */
  @Value("${via.sms.twilo.auth.token}")
  private String authToken;
  /**
   * TWILIO_NUMBER.
   */
  @Value("${via.sms.twilo.phonenumber}")
  private String twiloPhoneNumber;

  /**
   * Overridden method to implement text message service.
   *
   * @param content text message content
   */
  public void send(final Content content) {
    Twilio.init(accountSid, authToken);
    Message.creator(
            new PhoneNumber(content.getTo()),
            new PhoneNumber(twiloPhoneNumber),
            String.format("Otp generated at %s : %s. It will expire at %s",
                content.getBody().get("startTime"),
                content.getBody().get("otp"),
                content.getBody().get("expiryTime")))
        .create();
  }
}
