package com.practice.integration;

import com.practice.VIAApplication;
import com.practice.currencyconverter.facade.CurrencyConverterFacade;
import com.practice.employee.repository.RatesRegisterRepository;
import com.practice.mail.service.EmailService;
import com.practice.scheduler.SchedulerFacade;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = VIAApplication.class)
@ActiveProfiles("test")
class SchedulerTest {

    @SpyBean
    private SchedulerFacade schedulerFacade;

    @MockBean
    EmailService emailService;

    @MockBean
    RatesRegisterRepository registerRepository;

    @MockBean
    CurrencyConverterFacade currencyConverterFacade;

    @Test
    void testScheduler() {
        Awaitility.await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> Mockito.verify(schedulerFacade,
                        Mockito.atMost(5)).dailyEmailAlertScheduler());
    }
}
