package com.practice.web.integration;

import static org.junit.jupiter.api.Assertions.fail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.practice.VIAApplication;
import com.practice.employee.service.DailyAlertSchedulerService;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = VIAApplication.class)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0, stubs = "classpath:/stubs/")
@TestPropertySource(properties = {
    "spring.datasource.url = jdbc:h2:mem:test",
    "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
    "spring.datasource.driverClassName = org.h2.Driver",
    "via.scheduler.cron.value = 0/60 * * * * ?"
})
@ActiveProfiles("test")
public class SchedulerEmailTest {

  private DailyAlertSchedulerService dailyAlertSchedulerService;
  private static GreenMail greenMail;

  @MockBean
  HttpRequest httpRequest;

  @Autowired
  public void setDailyAlertSchedulerService(DailyAlertSchedulerService dailyAlertSchedulerService) {
    this.dailyAlertSchedulerService = dailyAlertSchedulerService;
  }

  private static void setupSMTP() {
    greenMail = new GreenMail(new ServerSetup(2525, "127.0.0.1", "smtp"));
    greenMail.start();
  }

  private static void tearDownSMTP() {
    greenMail.stop();
  }

  @Test
  void testGetScheduledCurrencyRateWithNoRegistration() {
    //GIVEN
    setupSMTP();
    //WHEN
    dailyAlertSchedulerService.dailyEmailAlertScheduler();
    //THEN
    boolean ok = greenMail.waitForIncomingEmail(0);
    if (ok) {
      Assertions.assertEquals(0, greenMail.getReceivedMessages().length);
    }
    tearDownSMTP();
  }

  @Sql("classpath:registerSameBaseAndTarget.sql")
  @Test
  void testGetScheduledCurrencyRateWithSameBaseAndTarget() throws MessagingException {
    //GIVEN
    setupSMTP();
    //WHEN
    dailyAlertSchedulerService.dailyEmailAlertScheduler();
    //THEN
    boolean ok = greenMail.waitForIncomingEmail(2);
    if (ok) {
      Assertions.assertEquals(2, greenMail.getReceivedMessages()[0].getAllRecipients().length);
      Assertions.assertEquals("employee@gmail.com",
          greenMail.getReceivedMessages()[0].getAllRecipients()[0].toString());
      Assertions.assertEquals("employee1@gmail.com",
          greenMail.getReceivedMessages()[0].getAllRecipients()[1].toString());
    } else {
      fail("Email not sent");
    }

    tearDownSMTP();
  }

  @Sql("classpath:registerDifferentBaseAndTarget.sql")
  @Test
  void testGetScheduledCurrencyRateWithDifferentBaseAndTarget() throws MessagingException {
    //GIVEN
    setupSMTP();
    //WHEN
    dailyAlertSchedulerService.dailyEmailAlertScheduler();
    //THEN
    boolean ok = greenMail.waitForIncomingEmail(2);
    if (ok) {
      Assertions.assertEquals(2, greenMail.getReceivedMessages().length);
      Assertions.assertEquals("employee@gmail.com",
          greenMail.getReceivedMessages()[0].getAllRecipients()[0].toString());
      Assertions.assertEquals("employee1@gmail.com",
          greenMail.getReceivedMessages()[1].getAllRecipients()[0].toString());
    } else {
      fail("Email not sent");
    }

    tearDownSMTP();
  }
}
