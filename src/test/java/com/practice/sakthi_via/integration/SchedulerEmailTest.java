/*
 * Copyright (c) 2020.
 */

package com.practice.sakthi_via.integration;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.practice.sakthi_via.facade.SchedulerFacade;
import com.practice.sakthi_via.validator.TargetCode;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0, stubs = "classpath:/stubs/")
@TestPropertySource(properties = {
        "spring.datasource.url = jdbc:h2:mem:test",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
        "spring.datasource.driverClassName = org.h2.Driver"
})
@ActiveProfiles("test")
public class SchedulerEmailTest {

    private SchedulerFacade schedulerFacade;
    private static GreenMail greenMail;

    @MockBean
    HttpRequest httpRequest;

    @Autowired
    public void setSchedulerFacade(SchedulerFacade schedulerFacade) {
        this.schedulerFacade = schedulerFacade;
    }

    private static void setupSMTP() {
        greenMail = new GreenMail(new ServerSetup(2525, "127.0.0.1", "smtp"));
        greenMail.start();
    }

    private static void tearDownSMTP() {
        greenMail.stop();
    }

    @Test
    void testGetScheduledCurrencyRateWithNoRegistration(){
        //GIVEN
        setupSMTP();
        //WHEN
        schedulerFacade.getScheduledCurrencyRate();
        //THEN
        boolean ok = greenMail.waitForIncomingEmail(0);
        if(ok) {
            assertEquals(0, greenMail.getReceivedMessages().length);
        }
        tearDownSMTP();
    }

    @Sql("classpath:register.sql")
    @Test
    void testGetScheduledCurrencyRateWithTwoRegistration() throws MessagingException {
        //GIVEN
        setupSMTP();
        //WHEN
        schedulerFacade.getScheduledCurrencyRate();
        //THEN
        boolean ok = greenMail.waitForIncomingEmail(2);
        if (ok) {
            assertEquals("employee@gmail.com",
                    greenMail.getReceivedMessages()[0].getAllRecipients()[0].toString());
            assertEquals("employee1@gmail.com",
                    greenMail.getReceivedMessages()[1].getAllRecipients()[0].toString());
        } else {
            fail("Email not sent");
        }

        tearDownSMTP();
    }
}
